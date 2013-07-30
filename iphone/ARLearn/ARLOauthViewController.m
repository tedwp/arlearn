//
//  ARLOauthViewController.m
//  ARLearn
//
//  Created by Stefaan Ternier on 7/25/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLOauthViewController.h"

@interface ARLOauthViewController ()

@end

@implementation ARLOauthViewController

@synthesize scanView;
@synthesize oauthView;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.view.backgroundColor = [UIColor whiteColor];
    }
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
//    self.view.backgroundColor = [UIColor clearColor];
//    self.view.translatesAutoresizingMaskIntoConstraints = NO;
//
    self.backButton = [UIButton buttonWithType:UIButtonTypeRoundedRect];
    [self.backButton setTitle:@"back" forState:UIControlStateNormal];
    self.backButton.translatesAutoresizingMaskIntoConstraints = NO;
    [self.backButton addTarget:self action:@selector(backClick) forControlEvents:UIControlEventTouchUpInside];


    [self.view addSubview:self.backButton];
    
    
    self.oauthView = [[ARLOauthView alloc] init];
    self.oauthView.viewController = self;
    [self.view addSubview:self.oauthView];
    
    
    self.scanView = [[ARLScanAuthView alloc] init];
    self.scanView.viewController = self;
    [self.view addSubview:self.scanView];
    
    if ((self.interfaceOrientation == UIInterfaceOrientationLandscapeLeft) || (self.interfaceOrientation == UIInterfaceOrientationLandscapeRight)) {
        
        [self setLandscapeConstraints];
    } else {
        [self setPortraitConstraints];
    }
//    NSDictionary* network = [ARLNetwork oauthInfo];
//    NSLog(@"network %@", network);
    [self initOauthUrls];
}



- (void) setPortraitConstraints {

    [self.view removeConstraints:[self.view constraints]];
    
    NSDictionary * viewsDictionary =
    [[NSDictionary alloc] initWithObjectsAndKeys:
     self.backButton, @"backButton",
     self.scanView, @"scanView",
     self.oauthView, @"oauthView", nil];

    NSString* verticalContstraint = @"V:|-[backButton(==30)]-[scanView(==100)]-[oauthView(>=200)]-|";
    
    [self.view addConstraints:[NSLayoutConstraint
                               constraintsWithVisualFormat:verticalContstraint
                               options:NSLayoutFormatDirectionLeadingToTrailing
                               metrics:nil
                               views:viewsDictionary]];
    [self.view addConstraints:[NSLayoutConstraint
                               constraintsWithVisualFormat:@"H:|-[backButton]"
                               options:NSLayoutFormatDirectionLeadingToTrailing
                               metrics:nil
                               views:viewsDictionary]];
    [self.view addConstraints:[NSLayoutConstraint
                               constraintsWithVisualFormat:@"H:|-[oauthView]-|"
                               options:NSLayoutFormatDirectionLeadingToTrailing
                               metrics:nil
                               views:viewsDictionary]];
    [self.view addConstraints:[NSLayoutConstraint
                               constraintsWithVisualFormat:@"H:|-[scanView]-|"
                               options:NSLayoutFormatDirectionLeadingToTrailing
                               metrics:nil
                               views:viewsDictionary]];

}

- (void) setLandscapeConstraints {
    [self.view removeConstraints:[self.view constraints]];
    
    
    NSDictionary * viewsDictionary =
    [[NSDictionary alloc] initWithObjectsAndKeys:
     self.scanView, @"scanView",
     self.oauthView, @"oauthView", nil];
    
    NSString* horizontalContstraint = @"H:|-[scanView(==oauthView)]-[oauthView]-|";
    
    [self.view addConstraints:[NSLayoutConstraint
                               constraintsWithVisualFormat:horizontalContstraint
                               options:NSLayoutFormatDirectionLeadingToTrailing
                               metrics:nil
                               views:viewsDictionary]];
    [self.view addConstraints:[NSLayoutConstraint
                               constraintsWithVisualFormat:@"V:|-[oauthView]-|"
                               options:NSLayoutFormatDirectionLeadingToTrailing
                               metrics:nil
                               views:viewsDictionary]];
    [self.view addConstraints:[NSLayoutConstraint
                               constraintsWithVisualFormat:@"V:|-[scanView]-|"
                               options:NSLayoutFormatDirectionLeadingToTrailing
                               metrics:nil
                               views:viewsDictionary]];
    
}

-(void)willRotateToInterfaceOrientation: (UIInterfaceOrientation)orientation duration:(NSTimeInterval)duration {
    if ((orientation == UIInterfaceOrientationLandscapeLeft) || (orientation == UIInterfaceOrientationLandscapeRight)) {
        [self setLandscapeConstraints];
    } else {
        [self setPortraitConstraints];
    }
}

- (void)backClick {
    [self dismissViewControllerAnimated:TRUE completion:nil];
}


- (void) imagePickerController: (UIImagePickerController*) reader didFinishPickingMediaWithInfo: (NSDictionary*) info {

    id<NSFastEnumeration> results = [info objectForKey: ZBarReaderControllerResults];
    ZBarSymbol *symbol = nil;
    for(symbol in results)
        break;
    NSLog(@"scanned %@", symbol.data);
    
    NSDictionary * auth = [ARLNetwork anonymousLogin:symbol.data];
    if (!auth) {

        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"No network connection"
                                                        message:@"You must be connected to the internet to use this app."
                                                       delegate:self
                                              cancelButtonTitle:@"OK"
                                              otherButtonTitles:nil];
        [alert show];
    } else if (!auth || [auth objectForKey:@"error"]){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Token was not recognized"
                                                        message:@"You must be connected to the internet to use this app."
                                                       delegate:self
                                              cancelButtonTitle:@"OK"
                                              otherButtonTitles:nil];
        [alert show];
    } else {
        NSString * authString = [auth objectForKey:@"auth"];
        [[NSUserDefaults standardUserDefaults] setObject:authString forKey:@"auth"];
        NSDictionary *accountDetails = [ARLNetwork accountDetails];
        ARLAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
        [Account accountWithDictionary:accountDetails inManagedObjectContext:appDelegate.managedObjectContext];
        [ARLAccountDelegator resetAccount:appDelegate.managedObjectContext];
        [self.presentedViewController dismissViewControllerAnimated:NO completion:nil];
        [self dismissViewControllerAnimated:YES completion:nil];
    }

}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    [self.presentedViewController dismissViewControllerAnimated:YES completion:nil];

}

- (void) initOauthUrls {
    NSDictionary* network = [ARLNetwork oauthInfo];
    NSLog(@"network %@", network);
    for (NSDictionary* dict in [network objectForKey:@"oauthInfoList"]) {
        
        switch ([(NSNumber*)[dict objectForKey:@"providerId"] intValue]) {
            case 1:
                self.facebookLoginString = [NSString stringWithFormat:@"https://graph.facebook.com/oauth/authorize?client_id=%@&display=page&redirect_uri=%@&scope=publish_stream,email", [dict objectForKey:@"clientId"], [dict objectForKey:@"redirectUri"]];
                break;
            case 2:
                 self.googleLoginString = [NSString stringWithFormat:@"https://accounts.google.com/o/oauth2/auth?redirect_uri=%@&response_type=code&client_id=%@&approval_prompt=force&scope=profile+email", [dict objectForKey:@"redirectUri"], [dict objectForKey:@"clientId"]];
                break;
            case 3:
                self.linkedInLoginString = [NSString stringWithFormat:@"https://www.linkedin.com/uas/oauth2/authorization?response_type=code&client_id=%@&scope=r_fullprofile+r_emailaddress+r_network&state=BdhOU9fFb6JcK5BmoDeOZbaY58&redirect_uri=%@", [dict objectForKey:@"clientId"], [dict objectForKey:@"redirectUri"]];
                break;
            case 4:
                self.twitterLoginString = [NSString stringWithFormat:@"%@?twitter=init", [dict objectForKey:@"redirectUri"]];

                break;

        }
    }
    
}

//- (void) googleOauth {
//    if (self.googleLoginString) {
//        ARLOauthWebViewController* svc = [[ARLOauthWebViewController alloc] init];
//        [svc setModalTransitionStyle:UIModalTransitionStyleCoverVertical];
//        [self presentViewController:svc animated:YES completion:nil];
//        [svc loadAuthenticateUrl:self.googleLoginString delegate:svc];
//    }
//}

- (void) oauth: (NSNumber *) providerId {
    NSString* loginString;
    switch ([providerId intValue]) {
        case 1:
            loginString = self.facebookLoginString;
            break;
        case 2:
            loginString = self.googleLoginString;
            break;
        case 3:
            loginString = self.linkedInLoginString;
            break;
        case 4:
            loginString = self.twitterLoginString;
            break;
        default:
            break;
    }
    
    if (loginString) {
        ARLOauthWebViewController* svc = [[ARLOauthWebViewController alloc] init];
//        svc.oauthListViewController = self;
        [self presentViewController:svc animated:YES completion:nil];
//        [svc setModalTransitionStyle:UIModalTransitionStyleCoverVertical];
//        [self presentViewController:svc animated:YES completion:nil];
        [svc loadAuthenticateUrl:loginString delegate:svc];
    }
}

@end
