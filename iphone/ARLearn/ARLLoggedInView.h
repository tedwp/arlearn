//
//  ARLLoggedInView.h
//  ARLearn
//
//  Created by Stefaan Ternier on 7/25/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <QuartzCore/QuartzCore.h>
#import "Account.h"
@interface ARLLoggedInView : UIView

@property (strong, nonatomic) UIImageView * accountImage;
@property (strong, nonatomic) UILabel * loggedInAsLabel;
@property (strong, nonatomic) UILabel * accountNameLabel;

-(void) setAccount: (Account*) account;

@end
