//
//  ARLMapViewController.m
//  ARLearn
//
//  Created by Stefaan Ternier on 1/17/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLMapViewController.h"

@interface ARLMapViewController ()

@end

@implementation ARLMapViewController

@synthesize run = _run;
//@synthesize generalItems = _generalItems;
//@synthesize gameId = _gameId;
@synthesize mapView = _mapView;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization

        
    }
    return self;
}


- (void)setupFetchedResultsController {
    NSNumber * currentTimeMillis = [NSNumber numberWithFloat:([[NSDate date] timeIntervalSince1970] * 1000 )];
    //    NSLog(@"current time %lld", [currentTimeMillis longLongValue]);
    NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"GeneralItem"];
    request.sortDescriptors = [NSArray arrayWithObject:[NSSortDescriptor sortDescriptorWithKey:@"name"
                                                                                     ascending:YES
                                                                                      selector:@selector(localizedCaseInsensitiveCompare:)]];
//    NSLog(@"ownerGame.gameId = %lld ",
//          [self.run.game.gameId longLongValue]);
//    request.predicate = [NSPredicate predicateWithFormat:
//                         @"ownerGame.gameId = %lld ",
//                         [self.run.game.gameId longLongValue]];

    request.predicate = [NSPredicate predicateWithFormat:
                         @"ownerGame.gameId = %lld and SUBQUERY(visibility, $x, $x.runId = %lld and $x.status = 1 and $x.timeStamp < %lld).@count > 0 and SUBQUERY(visibility, $x, $x.runId = %lld and $x.status = 2 and $x.timeStamp < %lld).@count = 0",
                         [self.run.game.gameId longLongValue], [self.run.runId longLongValue], [currentTimeMillis longLongValue], self.run.runId, [currentTimeMillis longLongValue]];
    
    
    self.fetchedResultsController = [[NSFetchedResultsController alloc] initWithFetchRequest:request
                                                                        managedObjectContext:self.run.managedObjectContext
                                                                          sectionNameKeyPath:nil
                                                                                   cacheName:nil];
}

- (void) setRun: (Run *) run {
    _run = run;
    self.title = run.title;
     [[self tabBarItem] setFinishedSelectedImage:[UIImage imageNamed:@"icon_maps.png"] withFinishedUnselectedImage:[UIImage imageNamed:@"icon_maps.png"]];


}

-(void) viewDidAppear:(BOOL)animated {
    NSLog(@"viewDidAppear %@", self.mapView);

    [self setupFetchedResultsController];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    NSLog(@"viewDidLoad %@", self.mapView);
    
    [self.mapView setMapType:MKMapTypeStandard];
    [self.mapView setZoomEnabled:YES];
    [self.mapView setScrollEnabled:YES];
    MKCoordinateRegion region = { {0.0, 0.0 }, { 0.0, 0.0 } };
    region.center.latitude = 51.03 ;
    region.center.longitude = 5.72;
    region.span.longitudeDelta = 0.01f;
    region.span.latitudeDelta = 0.01f;
    [self.mapView setRegion:region animated:YES];
    
            [self.mapView setDelegate:self];

}

- (void) fetchReady  {
    NSLog(@"ready fetching old %d", [[[self.fetchedResultsController sections] objectAtIndex:0] numberOfObjects]);
    for (GeneralItem * gi in self.fetchedResultsController.fetchedObjects) {
    NSLog(@"gi %@", gi.name);

        GiMap * mapItem = [[GiMap alloc] init];
        mapItem.title = gi.name;
        mapItem.subtitle = gi.descriptionText;
        mapItem.itemId = [NSNumber numberWithLongLong:gi.id];
        mapItem.generalItem = gi;
        CLLocationCoordinate2D coords;
        coords.latitude = [gi.lat doubleValue];
        coords.longitude =[gi.lng doubleValue];
        
        mapItem.coordinate = coords;
        [[self mapView] addAnnotation:mapItem];


    }
}


- (void)fetchedResultsChangeUpdate:(GeneralItem *)gi  {
    NSLog(@"fetchedResultsChangeUpdate %@", gi.name);
    for (GiMap * mapItem in self.mapView.annotations) {
        if ([mapItem.itemId isEqualToNumber:[NSNumber numberWithLongLong:gi.id]]) {
            mapItem.title = gi.name;
            mapItem.subtitle = gi.descriptionText;
            mapItem.itemId = [NSNumber numberWithLongLong:gi.id];
            mapItem.generalItem = gi;
            CLLocationCoordinate2D coords;
            coords.latitude = [gi.lat doubleValue];
            coords.longitude =[gi.lng doubleValue];

            mapItem.coordinate = coords;

        }
    }   
}

- (void)fetchedResultsChangeDelete:(GeneralItem*)gi  {
    NSLog(@"fetchedResultsChangeDelete %@", gi);
    NSLog(@"fetchedResultsChangeDelete delete %@", gi.name);
    for (GiMap * mapItem in self.mapView.annotations) {
        if ([mapItem.itemId isEqualToNumber:[NSNumber numberWithLongLong:gi.id]]) {
            [self.mapView removeAnnotation:mapItem];
        }
    }
}

- (MKAnnotationView *)mapView:(MKMapView *)mapView viewForAnnotation:(id < MKAnnotation >)annotation {
    static NSString *identifier = @"MyLocation";
    if ([annotation isKindOfClass:[GiMap class]]) {
        GeneralItem * gi = [(GiMap*)annotation  generalItem];
        
        NSLog(@"gi tilt %@", gi.name);
        
        MKAnnotationView * annotationView = [[MKAnnotationView alloc ] initWithAnnotation:annotation reuseIdentifier:identifier];
        annotationView.image = [ UIImage imageNamed:@"black_stop.png" ];
       
        NSData * icon = [gi customIconData];
        if (icon) {
            UIImage * image = [UIImage imageWithData:icon];
            annotationView.image = image;
        }
        
//        MKPinAnnotationView *annotationView =
//        (MKPinAnnotationView *)[self.mapView dequeueReusableAnnotationViewWithIdentifier:identifier];
        
        if (annotationView == nil) {
            annotationView = [[MKPinAnnotationView alloc]
                              initWithAnnotation:annotation
                              reuseIdentifier:identifier];
        } else {
            annotationView.annotation = annotation;
        }
        
        annotationView.enabled = YES;
        annotationView.canShowCallout = YES;

        
        UIButton *rightButton = [UIButton buttonWithType:UIButtonTypeDetailDisclosure];
        [rightButton setTitle:annotation.title forState:UIControlStateNormal];
        [annotationView setRightCalloutAccessoryView:rightButton];
        
        return annotationView;
    }
    return nil;
}

- (void)mapView:(MKMapView *)mapView annotationView:(MKAnnotationView *)view calloutAccessoryControlTapped:(UIControl *)control {
    
    if ([(UIButton*)control buttonType] == UIButtonTypeDetailDisclosure){

        UIStoryboard *storyboard = self.storyboard;
        GeneralItem* generalItem = [(GiMap*)[view annotation] generalItem];
        UITableViewController *mapDetailViewController = [storyboard instantiateViewControllerWithIdentifier:generalItem.type];
//        ARLGeneralItemViewController *mapDetailViewController = [storyboard instantiateViewControllerWithIdentifier:@"generalItemViewControlleridentifier"];
//        mapDetailViewController.generalItem =[(GiMap*)[view annotation] generalItem];
        
        if ([mapDetailViewController respondsToSelector:@selector(setGeneralItem:)]) {
            [mapDetailViewController performSelector:@selector(setGeneralItem:) withObject:generalItem];
        }
        if ([mapDetailViewController respondsToSelector:@selector(setRun:)]) {
            [mapDetailViewController performSelector:@selector(setRun:) withObject:self.run];
        }
        
        [[self navigationController] pushViewController:mapDetailViewController animated:YES];
        
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end


@implementation GiMap
@synthesize coordinate,title,subtitle, itemId, generalItem ;


- (MKMapItem*)mapItem {
    
    MKPlacemark *placemark = [[MKPlacemark alloc] initWithCoordinate:self.coordinate addressDictionary:nil];
                              
    MKMapItem *mapItem = [[MKMapItem alloc] initWithPlacemark:placemark];
    mapItem.name = self.title;
    
    return mapItem;
}

@end