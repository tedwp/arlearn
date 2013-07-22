//
//  ARLGeneralItemTableViewController.h
//  ARLearn
//
//  Created by Stefaan Ternier on 1/14/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ARLBean.h"
#import "ARLGeneralItemViewController.h"
#import "ARLMultipleChoiceTestViewController.h"
#import "ARLScanTagViewController.h"
#import "ARLNetwork.h"
#import "Run.h"
#import "Game.h"
#import "GeneralItem+ARLearnBeanCreate.h"
#import "GeneralItemVisibility.h"
#import "CoreDataTableViewController.h"


@interface ARLGeneralItemTableViewController : CoreDataTableViewController 

@property (nonatomic, strong) Run *run;

@end
