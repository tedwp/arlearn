//
//  ARLGamesMapViewController.m
//  ARLearn
//
//  Created by Stefaan Ternier on 8/13/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLGamesMapViewController.h"

@interface ARLGamesMapViewController ()

@end

@implementation ARLGamesMapViewController

@synthesize searchArray;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
    
    [self.map setMapType:MKMapTypeStandard];
    [self.map setZoomEnabled:YES];
    [self.map setScrollEnabled:YES];
    MKCoordinateRegion region = { {0.0, 0.0 }, { 0.0, 0.0 } };
    region.center.latitude = 51.03 ;
    region.center.longitude = 5.72;
    region.span.longitudeDelta = 0.1f;
    region.span.latitudeDelta = 0.1f;
    [self.map setRegion:region animated:YES];
    [self.map showsUserLocation];
    
    [self.map setDelegate:self];
}

- (void) viewDidAppear:(BOOL)animated {
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)mapView:(MKMapView *)mapView regionDidChangeAnimated:(BOOL)animated {
    NSDictionary* result = [ARLNetwork geoSearch: [NSNumber numberWithInt:1000000]
                                         withLat:[NSNumber numberWithDouble:mapView.region.center.latitude]
                                         withLng:[NSNumber numberWithDouble:mapView.region.center.longitude]];
    
    self.searchArray = [NSMutableArray arrayWithArray:[result objectForKey:@"games"]];
    for (NSDictionary * game in [result objectForKey:@"games"]) {
        GameMap* mapItem = [[GameMap alloc] init];
        mapItem.title = [game objectForKey:@"title"];
        mapItem.itemId =[game objectForKey:@"gameId"];
        NSNumber * lat = [game objectForKey:@"lat"];
        NSNumber * lng = [game objectForKey:@"lng"];
        CLLocationCoordinate2D coords;
        coords.latitude = (CLLocationDegrees) lat.doubleValue;
        coords.longitude = (CLLocationDegrees) lng.doubleValue;
        
        mapItem.coordinate = coords;
        [self.map addAnnotation:mapItem];
    }
    
}

- (MKAnnotationView *)mapView:(MKMapView *)mapView viewForAnnotation:(id < MKAnnotation >)annotation {
    static NSString *identifier = @"MyLocation";
    if ([annotation isKindOfClass:[GameMap class]]) {
        MKPinAnnotationView *annotationView =
        (MKPinAnnotationView *)[self.map dequeueReusableAnnotationViewWithIdentifier:identifier];
        
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
        NSNumber* gameId = [(GameMap*)[view annotation] itemId];
        UITableViewController *mapDetailViewController = [storyboard instantiateViewControllerWithIdentifier:@"gameOverviewPage"];
        ARLAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
        Game* game = [Game gameWithDictionary:[ARLNetwork game:gameId] inManagedObjectContext:appDelegate.managedObjectContext];
        if ([mapDetailViewController respondsToSelector:@selector(setGame:)]) {
            [mapDetailViewController performSelector:@selector(setGame:) withObject:game];
        }
        
        
        
        [[self navigationController] pushViewController:mapDetailViewController animated:YES];
        
    }
}

@end

@implementation GameMap
@synthesize coordinate,title,subtitle, itemId;


- (MKMapItem*)mapItem {
    
    MKPlacemark *placemark = [[MKPlacemark alloc] initWithCoordinate:self.coordinate addressDictionary:nil];
    
    MKMapItem *mapItem = [[MKMapItem alloc] initWithPlacemark:placemark];
    mapItem.name = self.title;
    
    return mapItem;
}

@end
