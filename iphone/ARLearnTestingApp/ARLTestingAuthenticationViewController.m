//
//  ARLTestingAuthenticationViewController.m
//  ARLearn
//
//  Created by Stefaan Ternier on 1/26/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLTestingAuthenticationViewController.h"

@interface ARLTestingAuthenticationViewController ()

@end

@implementation ARLTestingAuthenticationViewController

@synthesize username;
@synthesize password;
@synthesize authKey;

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

- (IBAction)authenticate:(id)sender {
    [[NSUserDefaults standardUserDefaults] setObject:self.username.text forKey:@"username"];
    [[NSUserDefaults standardUserDefaults] synchronize];
    NSString * token = [ARLNetwork requestAuthToken:self.username.text password:self.password.text];
    self.authKey.text = token;
    [[NSUserDefaults standardUserDefaults] setObject:token forKey:@"auth"];
    [[NSUserDefaults standardUserDefaults] synchronize];
}

//- (void) authenticationTokenReturned: (NSString *) token {
//    self.authKey.text = token;
//    [[NSUserDefaults standardUserDefaults] setObject:token forKey:@"auth"];
//    [[NSUserDefaults standardUserDefaults] synchronize];
//     NSLog(@"auth is now %@", [[NSUserDefaults standardUserDefaults]objectForKey:@"auth"]);
//}
//
//- (void) authenticationFailed {
//    self.authKey.text = @"authentication failed";
//}
//- (void) noNetwork {
//    self.authKey.text = @"no network";
//}
//
@end
