//
//  ARLOauthViewController.h
//  ARLearn
//
//  Created by Stefaan Ternier on 7/25/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ARLScanAuthView.h"
#import "ARLOauthView.h"
#import "ZBarSDK.h"
#import "Account+Create.h"
#import "ARLAppDelegate.h"
#import "ARLAccountDelegator.h"
#import "ARLOauthWebViewController.h"

@class ARLOauthView;

@interface ARLOauthViewController : UIViewController < ZBarReaderDelegate >

@property (strong, nonatomic) UIButton * backButton;
@property (strong, nonatomic) ARLScanAuthView * scanView;
@property (strong, nonatomic) ARLOauthView * oauthView;


@property (strong, nonatomic) NSString * facebookLoginString;
@property (strong, nonatomic) NSString * googleLoginString;
@property (strong, nonatomic) NSString * linkedInLoginString;
@property (strong, nonatomic) NSString * twitterLoginString;


- (void) oauth: (NSNumber *) providerId ;
@end
