//
//  ARLOauthView.h
//  ARLearn
//
//  Created by Stefaan Ternier on 7/25/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <QuartzCore/QuartzCore.h>
#import "ARLOauthViewController.h"

@class ARLOauthViewController;

@interface ARLOauthView : UIView

@property (strong, nonatomic) UILabel * authLabel;

@property (strong, nonatomic) UIButton * twitterImage;
@property (strong, nonatomic) UIButton * googleImage;
@property (strong, nonatomic) UIButton * facebookImage;
@property (strong, nonatomic) UIButton * linkedInImage;

@property (strong, nonatomic) ARLOauthViewController * viewController;

@end
