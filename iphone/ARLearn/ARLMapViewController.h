//
//  ARLMapViewController.h
//  ARLearn
//
//  Created by Stefaan Ternier on 1/17/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import "ARLGeneralItemViewController.h"
#import "Run.h"
#import "Game.h"
#import "GeneralItem.h"
#import "ARLMapTableViewController.h"

@interface ARLMapViewController : ARLMapTableViewController <MKMapViewDelegate>
@property (nonatomic, strong) Run *run;

//@property (nonatomic, strong) NSArray *generalItems;
//@property (nonatomic, strong) NSNumber * gameId;
@property (weak, nonatomic) IBOutlet MKMapView *mapView;
@end



@interface GiMap : NSObject <MKAnnotation> {
    CLLocationCoordinate2D coordinate;
    NSString *title;
    NSString *subtitle;
}

@property (nonatomic, assign) CLLocationCoordinate2D coordinate;
@property (nonatomic, copy) NSString *title;
@property (nonatomic, copy) NSString *subtitle;
@property (nonatomic, copy) NSNumber *itemId;
@property (nonatomic, weak) GeneralItem * generalItem;


@end