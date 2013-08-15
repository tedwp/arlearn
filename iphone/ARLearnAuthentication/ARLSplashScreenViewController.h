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
#import "Account+Create.h"
#import "ARLLoggedInView.h"
#import "ARLRunTableViewController.h"

#import "ARLOauthViewController.h"
#import "ARLOauthListViewController.h"
#import "ARLGradientButton.h"


@interface ARLSplashScreenViewController : UIViewController

@property (strong, nonatomic) Account* account;
@property (strong, nonatomic) UIButton* loginButton;
@property (strong, nonatomic) UIButton* myRunsButton;
@property (strong, nonatomic) UIButton* gamesButton;
@property (strong, nonatomic) UIImageView* arlearnImage;
@property (strong, nonatomic) ARLLoggedInView* loggedInView;

- (void) createViewsProgrammatically ;
@end
