//
//  ARLTestingAuthenticationViewController.h
//  ARLearn
//
//  Created by Stefaan Ternier on 1/26/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ARLNetwork.h"

@interface ARLTestingAuthenticationViewController : UIViewController 

- (IBAction)authenticate:(id)sender;
@property (weak, nonatomic) IBOutlet UITextField *username;
@property (weak, nonatomic) IBOutlet UITextField *password;
@property (weak, nonatomic) IBOutlet UITextView *authKey;

@end
