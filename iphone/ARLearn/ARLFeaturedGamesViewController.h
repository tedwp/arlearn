//
//  ARLFeaturedGamesViewController.h
//  ARLearn
//
//  Created by Stefaan Ternier on 8/14/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ARLNetwork.h"
#import "ARLAppDelegate.h"
#import "Game+ARLearnBeanCreate.h"

@interface ARLFeaturedGamesViewController : UITableViewController

@property (nonatomic, strong) NSArray * searchArray;

@end
