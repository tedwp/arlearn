//
//  ARLScanObjectViewController.h
//  ARLearn
//
//  Created by Stefaan Ternier on 7/9/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "GeneralItem+ARLearnBeanCreate.h"
#import "Run.h"
#import "Action+Create.h"
#import "ZBarSDK.h"
#import "ARLCloudSynchronizer.h"

@interface ARLScanObjectViewController : UIViewController  < ZBarReaderDelegate >

@property (strong, nonatomic) GeneralItem * generalItem;
@property (strong, nonatomic) Run * run;

- (IBAction)scanTagButton:(id)sender;

@end
