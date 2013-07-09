//
//  ARLTestingRunViewController.h
//  ARLearn
//
//  Created by Stefaan Ternier on 1/26/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ARLNetwork.h"

@interface ARLTestingRunViewController : UIViewController
- (IBAction)allRuns:(id)sender;
- (IBAction)withRunId:(id)sender;
@property (weak, nonatomic) IBOutlet UITextView *resultPane;
@property (weak, nonatomic) IBOutlet UITextField *runIdField;


@end
