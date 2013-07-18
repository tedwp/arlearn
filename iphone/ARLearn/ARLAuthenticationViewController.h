//
//  ARLAuthenticationViewController.h
//  ARLearn
//
//  Created by Stefaan Ternier on 1/26/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ARLNetwork.h"
#import "ARLDataCollectionWidget.h"

@interface ARLAuthenticationViewController : UIViewController
@property (weak, nonatomic) IBOutlet UITextField *email;
@property (weak, nonatomic) IBOutlet UITextField *password;

- (IBAction)login:(id)sender;
- (IBAction)back:(id)sender;

@end
