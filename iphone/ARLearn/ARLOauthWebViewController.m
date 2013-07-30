//
//  ARLOauthWebViewController.m
//  ARLearn
//
//  Created by Stefaan Ternier on 6/24/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLOauthWebViewController.h"
#import "ARLAccountDelegator.h"
#import "ARLAppDelegate.h"
#import "Account+create.h"

@interface ARLOauthWebViewController ()

@end

@implementation ARLOauthWebViewController

//@synthesize oauthList = _oauthList;
//@synthesize selfRef;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.selfRef = self;
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.webView = [[UIWebView alloc] init];
    self.webView.translatesAutoresizingMaskIntoConstraints = NO;

    [self.view addSubview:self.webView];
    
    NSDictionary * viewsDictionary =
    [[NSDictionary alloc] initWithObjectsAndKeys: self.webView, @"webView", nil];
    
    [self.view addConstraints:[NSLayoutConstraint
                          constraintsWithVisualFormat:@"H:|[webView]|"
                          options:nil
                          metrics:nil
                          views:viewsDictionary]];
    [self.view addConstraints:[NSLayoutConstraint
                          constraintsWithVisualFormat:@"V:|[webView]|"
                          options:nil
                          metrics:nil
                          views:viewsDictionary]];
	// Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)loadAuthenticateUrl:(NSString *)authenticateUrl delegate:(id) aDelegate {
    [self deleteARLearnCookie];
//    self.delegate = aDelegate;
    self.webView.delegate = self;
    self.webView.scalesPageToFit = YES;
    self.domain = [[NSURL URLWithString:authenticateUrl] host];
    [self.webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:authenticateUrl]]];
}

- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType {
    NSString * urlAsString =request.URL.description;
    if (!([urlAsString rangeOfString:@"twitter?denied="].location == NSNotFound)) {
//        [self.presentingViewController dismissViewControllerAnimated:YES completion:nil];
        [self close];
        return YES;
    }

    if (!([urlAsString rangeOfString:@"error=access_denied"].location == NSNotFound)) {
        [self close];
        return YES;
    }
    
    if (!([urlAsString rangeOfString:@"oauth.html?accessToken="].location == NSNotFound)) {
        NSArray *listItems = [urlAsString componentsSeparatedByString:@"accessToken="];
        NSString * lastObject =[listItems lastObject];
        listItems = [lastObject componentsSeparatedByString:@"&"];
        [[NSUserDefaults standardUserDefaults] setObject:[listItems objectAtIndex:0] forKey:@"auth"];
        ARLAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
        NSDictionary *accountDetails = [ARLNetwork accountDetails];

        [Account accountWithDictionary:accountDetails inManagedObjectContext:appDelegate.managedObjectContext];
        [[NSUserDefaults standardUserDefaults] setObject:[accountDetails objectForKey:@"localId"] forKey:@"accountLocalId"];
        [[NSUserDefaults standardUserDefaults] setObject:[accountDetails objectForKey:@"accountType"] forKey:@"accountType"];

        NSString *fullId = [NSString stringWithFormat:@"%@:%@",  [accountDetails objectForKey:@"accountType"], [accountDetails objectForKey:@"localId"]];        
        [[ARLNotificationSubscriber sharedSingleton] registerAccount:fullId];
        [self close];
        return YES;
        
    } else if (!([urlAsString rangeOfString:@"about:blank"].location == NSNotFound)) {
        return YES;
    } else if (!([urlAsString rangeOfString:@"accounts.google.com/o/oauth2/approval?"].location == NSNotFound)) {
        return YES;
    } else if (!([urlAsString rangeOfString:@"google?code="].location == NSNotFound)) {
        return YES;
    } else if (!([urlAsString rangeOfString:@"o/oauth2/auth?redirect_uri"].location == NSNotFound)) {
        return YES;
    } else if (!([urlAsString rangeOfString:@"appspot.com/oauth"].location == NSNotFound)) {
        return YES;
    }else if (!([urlAsString rangeOfString:@"authenticate?oauth_token="].location == NSNotFound)) {
        return YES;
    }else if (!([urlAsString rangeOfString:@"oauth/twitter?oauth_token="].location == NSNotFound)) {
        return YES;
    }else if (!([urlAsString rangeOfString:@"https://api.twitter.com/oauth/authenticate"].location == NSNotFound)) {
        return YES;
    }else if (!([urlAsString rangeOfString:@"dialog/oauth"].location == NSNotFound)) {
        return YES;
    }else {
        
        NSLog(@"not found %@", urlAsString);
//        [self close];
        return YES;

    }
    return YES;
    
}

-(void) close {
    if([NSThread isMainThread]) {
        [self.presentingViewController.presentingViewController dismissViewControllerAnimated:NO completion:nil];
        [self.presentingViewController dismissViewControllerAnimated:YES completion:nil];
        self.selfRef = nil;
    } else {
        [self performSelectorOnMainThread:@selector(close)
                               withObject:nil
                            waitUntilDone:YES];
    }
}
//
//- (void)webViewDidFinishLoad:(UIWebView *)theWebView {
//                    NSLog(@"web did finish loading");
//    NSString* token = [self getTokenFromCookie];
//    if (token != nil) {
////        [self.delegate gotToken:token];
////        [self.navigationController popViewControllerAnimated:YES];
////        [self.oauthList disappear];
//        [self dismissViewControllerAnimated:YES completion:nil];
//
////        [self.oauthList dismissViewControllerAnimated:YES completion:nil];
//    }
//}
//
//- (NSString*)getTokenFromCookie {
//    NSHTTPCookie *cookie;
//    NSHTTPCookieStorage *cookieJar = [NSHTTPCookieStorage sharedHTTPCookieStorage];
//    for (cookie in [cookieJar cookies]) {
//            if ([[cookie name] isEqualToString:@"arlearn.AccessToken"]) {
//                NSLog(@"got cookie");
//                NSLog([NSString stringWithFormat:@"cookie = %@", [cookie value]]);
//                [[NSUserDefaults standardUserDefaults] setObject:[cookie value] forKey:@"auth"];
//                ARLAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
//
//                       [ARLAccountDelegator resetAccount:appDelegate.arlearnDatabase.managedObjectContext];
//                return [cookie value];
//            }
////        }
//    }
//    return nil;
//}


- (void) deleteARLearnCookie {
    NSHTTPCookie *cookie;
    NSHTTPCookieStorage *cookieJar = [NSHTTPCookieStorage sharedHTTPCookieStorage];
    for (cookie in [cookieJar cookies]) {
        if ([[cookie name] isEqualToString:@"arlearn.AccessToken"]) {
            [cookieJar deleteCookie:cookie];
        }
    }
}


@end
