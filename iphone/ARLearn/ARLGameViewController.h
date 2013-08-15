//
//  ARLGameViewController.h
//  ARLearn
//
//  Created by Stefaan Ternier on 8/13/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Game+ARLearnBeanCreate.h"
#import <QuartzCore/QuartzCore.h>
#import "ARLNetwork.h"

@interface ARLGameViewController : UIViewController
@property (weak, nonatomic) IBOutlet UINavigationItem *navBar;
@property (weak, nonatomic) UIWebView *webView;
@property (weak, nonatomic) Game *game;
@property (weak, nonatomic) UIButton * playButton;

@property (weak, nonatomic) CAGradientLayer *gradient;
@property (strong, nonatomic) UIActivityIndicatorView *busyIndicator;
@end
