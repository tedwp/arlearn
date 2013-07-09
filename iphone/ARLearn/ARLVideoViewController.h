//
//  ARLVideoViewController.h
//  ARLearn
//
//  Created by Stefaan Ternier on 7/5/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "GeneralItem.h"
#import "GeneralItemData.h"
@interface ARLVideoViewController : UIViewController
@property (strong, nonatomic) GeneralItem * generalItem;
@property (weak, nonatomic) IBOutlet UINavigationItem *headerText;
- (IBAction)playVideo:(id)sender;

@end
