//
//  ARLRunTableViewController.h
//  ARLearn
//
//  Created by Stefaan Ternier on 1/13/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ARLAppDelegate.h"
#import "ARLRunViewCell.h"
#import "ARLGeneralItemTableViewController.h"
#import "ARLMapViewController.h"
#import "CoreDataTableViewController.h"
#import "Run+ARLearnBeanCreate.h"
#import "Game+ARLearnBeanCreate.h"
#import "GeneralItem+ARLearnBeanCreate.h"
#import "GeneralItemVisibility+ARLearnBeanCreate.h"
#import "ARLFileCloudSynchronizer.h"

@interface ARLRunTableViewController : CoreDataTableViewController

@property (weak, nonatomic) IBOutlet UIBarButtonItem *backButton;


@end
