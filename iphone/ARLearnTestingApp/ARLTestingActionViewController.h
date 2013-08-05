//
//  ARLTestingActionViewController.h
//  ARLearn
//
//  Created by Stefaan Ternier on 2/1/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ARLNetwork.h"

@interface ARLTestingActionViewController : UIViewController
@property (weak, nonatomic) IBOutlet UITextField *action;
@property (weak, nonatomic) IBOutlet UITextField *runId;
@property (weak, nonatomic) IBOutlet UITextField *user;
@property (weak, nonatomic) IBOutlet UITextField *giType;
- (IBAction)submitAction:(id)sender;
- (IBAction)submitReadAction:(id)sender;

@end
