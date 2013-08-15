//
//  ARLGamesMapViewController.h
//  ARLearn
//
//  Created by Stefaan Ternier on 8/13/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import "ARLNetwork.h"
#import "Game+ARLearnBeanCreate.h"
#import "ARLAppDelegate.h"


@interface ARLGamesMapViewController : UIViewController <MKMapViewDelegate>

@property (strong, nonatomic) IBOutlet MKMapView *map;

@property (nonatomic, strong) NSArray * searchArray;

@end


@interface GameMap : NSObject <MKAnnotation> {
    CLLocationCoordinate2D coordinate;
    NSString *title;
    NSString *subtitle;
}

@property (nonatomic, assign) CLLocationCoordinate2D coordinate;
@property (nonatomic, copy) NSString *title;
@property (nonatomic, copy) NSString *subtitle;
@property (nonatomic, copy) NSNumber *itemId;

@end