//
//  ARLSplashScreenViewController.h
//  ARLearn
//
//  Created by Stefaan Ternier on 7/10/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <QuartzCore/QuartzCore.h>
#import "ARLAppDelegate.h"
#import "Account+create.h"

@interface ARLSplashScreenViewController : UIViewController
@property (weak, nonatomic) IBOutlet UIView *loggedInView;
@property (weak, nonatomic) IBOutlet UIImageView *imageView;
@property (weak, nonatomic) IBOutlet UILabel *userName;

@end
