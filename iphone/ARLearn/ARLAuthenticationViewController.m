//
//  ARLAuthenticationViewController.m
//  ARLearn
//
//  Created by Stefaan Ternier on 1/26/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLAuthenticationViewController.h"

@interface ARLAuthenticationViewController ()

@end

@implementation ARLAuthenticationViewController

UIActivityIndicatorView *spinner;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

- (IBAction)login:(id)sender {
    spinner = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleGray];
    spinner.center = CGPointMake(160, 240);
    spinner.hidesWhenStopped = YES;
    [self.view addSubview:spinner];
    [spinner startAnimating];
    NSString * username =self.email.text;
    NSString * password =self.password.text;
    dispatch_queue_t fetchQ = dispatch_queue_create("Authenticate Request Queue", NULL);
    dispatch_async(fetchQ, ^{
        NSString * authToken = [ARLNetwork requestAuthToken:username password:password];
        if (authToken) {
            [[NSUserDefaults standardUserDefaults] setObject:authToken forKey:@"auth"];
            [[NSUserDefaults standardUserDefaults] setObject:self.email.text forKey:@"username"];
            [[NSUserDefaults standardUserDefaults] synchronize];
            dispatch_async(dispatch_get_main_queue(), ^{
                [spinner stopAnimating];
                [self dismissViewControllerAnimated:YES completion:nil];
            });
        } else {
            dispatch_async(dispatch_get_main_queue(), ^{
                [spinner stopAnimating];
                UIAlertView *message = [[UIAlertView alloc] initWithTitle:@"Login failed"
                                                                  message:@"Your Email or Password is invalid."
                                                                 delegate:nil
                                                        cancelButtonTitle:@"OK"
                                                        otherButtonTitles:nil];
                [message show];
            });
        }
    });
    [self.view endEditing:YES];
    
}

- (IBAction)back:(id)sender {
    [self dismissViewControllerAnimated:YES completion:nil];
    [self.view endEditing:YES];
}

@end
