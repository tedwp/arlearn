//
//  ARLOauthWebViewController.h
//  ARLearn
//
//  Created by Stefaan Ternier on 6/24/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <UIKit/UIKit.h>


@class ARLOauthListViewController;

@interface ARLOauthWebViewController : UIViewController  

@property (weak, nonatomic) IBOutlet UIWebView *webView;
@property (nonatomic, assign) IBOutlet NSObject<UIWebViewDelegate> *delegate;
@property (weak, nonatomic) NSString * domain;
//@property (weak, nonatomic) ARLOauthListViewController *oauthList;


- (void)loadAuthenticateUrl:(NSString *)authenticateUrl delegate:(id) aDelegate;
@end
