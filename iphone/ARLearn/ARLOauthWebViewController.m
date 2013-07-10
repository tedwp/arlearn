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

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)loadAuthenticateUrl:(NSString *)authenticateUrl delegate:(id) aDelegate {
    [self deleteARLearnCookie];
    self.delegate = aDelegate;
    self.webView.delegate = self;
    self.webView.scalesPageToFit = YES;
    self.domain = [[NSURL URLWithString:authenticateUrl] host];
    [self.webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:authenticateUrl]]];
}

- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType {
    NSString * urlAsString =request.URL.description;
    if (!([urlAsString rangeOfString:@"oauth.html?accessToken="].location == NSNotFound)) {
        NSArray *listItems = [urlAsString componentsSeparatedByString:@"accessToken="];
        NSString * lastObject =[listItems lastObject];
        listItems = [lastObject componentsSeparatedByString:@"&"];
        [[NSUserDefaults standardUserDefaults] setObject:[listItems objectAtIndex:0] forKey:@"auth"];
        ARLAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
        NSDictionary *accountDetails = [ARLNetwork accountDetails];
        NSLog(@"accountDetails %@", accountDetails.description);
        [Account accountWithDictionary:accountDetails inManagedObjectContext:appDelegate.arlearnDatabase.managedObjectContext];
        [[NSUserDefaults standardUserDefaults] setObject:[accountDetails objectForKey:@"localId"] forKey:@"accountLocalId"];
        
        
        [ARLAccountDelegator resetAccount:appDelegate.arlearnDatabase.managedObjectContext];
        [self.presentingViewController dismissViewControllerAnimated:YES completion:nil];

        return NO;
        
    } else {
        NSLog(@"not found %@", urlAsString);
    }
    return YES;
    
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
