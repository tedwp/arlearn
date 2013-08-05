//
//  ARLSplashScreenViewController.m
//  ARLearn
//
//  Created by Stefaan Ternier on 7/10/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLSplashScreenViewController.h"

@interface ARLSplashScreenViewController ()

@end

@implementation ARLSplashScreenViewController

@synthesize account;
@synthesize loginButton;
@synthesize arlearnImage;
@synthesize myRunsButton;

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
    
    [self.loggedInView.layer setCornerRadius:10.0f];
    [self.loggedInView.layer setBorderColor:[UIColor lightGrayColor].CGColor];
    [self.loggedInView.layer setBorderWidth:1.5f];
    
    
    //    [self createLoggedInImage]
}

- (void) viewWillAppear:(BOOL)animated {
    [self fetchCurrentAccount];
    [self createViewsProgrammatically];
    [self.loggedInView setAccount:self.account];
}


- (void) createViewsProgrammatically {
    if (self.account) {
        NSLog(@"we are logged in");
    } else {
        NSLog(@"we are not logged in");
    }
    [self createLoginButton];
    
    [self createARLearnImage];
    if (self.account) {
        [self createLoggedInView];
        [self createMyRunsButton];
    } else {
        [self.loggedInView removeFromSuperview];
        [self.myRunsButton removeFromSuperview];
        self.loggedInView = nil;
        self.myRunsButton = nil;
    }
    
    if ((self.interfaceOrientation == UIInterfaceOrientationLandscapeLeft) || (self.interfaceOrientation == UIInterfaceOrientationLandscapeRight)) {
        
        [self setLandscapeConstraints];
    } else {
        [self setPortraitConstraints];
    }
}

- (void) createLoggedInView {
    if (!self.loggedInView) {
        self.loggedInView = [[ARLLoggedInView alloc] init];
        [self.view addSubview:self.loggedInView];
    }
}

- (void) createARLearnImage {
    if (!self.arlearnImage) {
        self.arlearnImage = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"arlearn_logo.png"]];
        self.arlearnImage.translatesAutoresizingMaskIntoConstraints = NO;
        self.arlearnImage.contentMode = UIViewContentModeScaleAspectFit;
        [self.view addSubview:self.arlearnImage];
    }
}

- (void) createLoginButton {
    if (!self.loginButton) {
        self.loginButton = [UIButton buttonWithType:UIButtonTypeRoundedRect];
        self.loginButton.translatesAutoresizingMaskIntoConstraints = NO;
        [self.loginButton addTarget:self action:@selector(loginClicked) forControlEvents:UIControlEventTouchUpInside];
        
        [self.view addSubview:self.loginButton];
    }
    
    if (self.account) {
        [self.loginButton setTitle:NSLocalizedString(@"Logout", nil) forState:UIControlStateNormal];
    } else {
        [self.loginButton setTitle:NSLocalizedString(@"Login", nil) forState:UIControlStateNormal];
    }
    
}

- (void) createMyRunsButton {
    if (!self.myRunsButton) {
        self.myRunsButton = [UIButton buttonWithType:UIButtonTypeRoundedRect];
        self.myRunsButton.translatesAutoresizingMaskIntoConstraints = NO;
        [self.myRunsButton addTarget:self action:@selector(myRunsClicked) forControlEvents:UIControlEventTouchUpInside];
        [self.view addSubview:self.myRunsButton];
    }
    [self.myRunsButton setTitle:@"My Runs" forState:UIControlStateNormal];
    
}


- (void) setPortraitConstraints {
    [self.view removeConstraints:[self.view constraints]];
    NSDictionary * viewsDictionary;
    NSString* verticalContstraint;
    if (self.account) {
        viewsDictionary=
        [[NSDictionary alloc] initWithObjectsAndKeys:
         self.myRunsButton, @"myRunsButton",
         self.loginButton, @"loginButton",
         self.loggedInView, @"loggedInView",
         self.arlearnImage, @"arlearnImage", nil];
        verticalContstraint = @"V:|-[loggedInView(==100)]-[arlearnImage(==150)]-(>=10)-[loginButton]-[myRunsButton]-|";
        
        [self.view addConstraints:[NSLayoutConstraint
                                   constraintsWithVisualFormat:@"H:|-[myRunsButton]-|"
                                   options:NSLayoutFormatDirectionLeadingToTrailing
                                   metrics:nil
                                   views:viewsDictionary]];
        [self.view addConstraints:[NSLayoutConstraint
                                   constraintsWithVisualFormat:@"H:|-[loggedInView]-|"
                                   options:NSLayoutFormatDirectionLeadingToTrailing
                                   metrics:nil
                                   views:viewsDictionary]];
        
    } else {
        
        viewsDictionary=
        [[NSDictionary alloc] initWithObjectsAndKeys:
         self.loginButton, @"loginButton",
         self.arlearnImage, @"arlearnImage", nil];
        verticalContstraint = @"V:|-[arlearnImage(==150)]-(>=10)-[loginButton]-|";
        
    }
    
    
    
    [self.view addConstraints:[NSLayoutConstraint
                               constraintsWithVisualFormat:verticalContstraint
                               options:NSLayoutFormatDirectionLeadingToTrailing
                               metrics:nil
                               views:viewsDictionary]];
    [self.view addConstraints:[NSLayoutConstraint
                               constraintsWithVisualFormat:@"H:|-[loginButton]-|"
                               options:NSLayoutFormatDirectionLeadingToTrailing
                               metrics:nil
                               views:viewsDictionary]];
    
    [self.view addConstraints:[NSLayoutConstraint
                               constraintsWithVisualFormat:@"H:|-[arlearnImage]-|"
                               options:NSLayoutFormatDirectionLeadingToTrailing
                               metrics:nil
                               views:viewsDictionary]];
    
}


- (void) setLandscapeConstraints {
    [self.view removeConstraints:[self.view constraints]];
    NSDictionary * viewsDictionary =
    [[NSDictionary alloc] initWithObjectsAndKeys:
     self.myRunsButton, @"myRunsButton",
     self.loginButton, @"loginButton",
     self.loggedInView, @"loggedInView",
     self.arlearnImage, @"arlearnImage", nil];
    NSString* verticalContstraint1 = @"V:|-[loggedInView(==80)]-(>=20)-[loginButton]-[myRunsButton]-|";
    NSString* verticalContstraint2 = @"V:|-[arlearnImage]-|";
    
    [self.view addConstraints:[NSLayoutConstraint
                               constraintsWithVisualFormat:verticalContstraint1
                               options:NSLayoutFormatDirectionLeadingToTrailing
                               metrics:nil
                               views:viewsDictionary]];
    [self.view addConstraints:[NSLayoutConstraint
                               constraintsWithVisualFormat:verticalContstraint2
                               options:NSLayoutFormatDirectionLeadingToTrailing
                               metrics:nil
                               views:viewsDictionary]];
    
    [self.view addConstraints:[NSLayoutConstraint
                               constraintsWithVisualFormat:@"H:|-[arlearnImage(==200)]-[loginButton]-|"
                               options:NSLayoutFormatDirectionLeadingToTrailing
                               metrics:nil
                               views:viewsDictionary]];
    [self.view addConstraints:[NSLayoutConstraint
                               constraintsWithVisualFormat:@"H:|-[arlearnImage(==200)]-[myRunsButton]-|"
                               options:NSLayoutFormatDirectionLeadingToTrailing
                               metrics:nil
                               views:viewsDictionary]];
    [self.view addConstraints:[NSLayoutConstraint
                               constraintsWithVisualFormat:@"H:|-[arlearnImage(==200)]-[loggedInView]-|"
                               options:NSLayoutFormatDirectionLeadingToTrailing
                               metrics:nil
                               views:viewsDictionary]];
    
}


-(void)willRotateToInterfaceOrientation: (UIInterfaceOrientation)orientation duration:(NSTimeInterval)duration {
    NSLog(@"amout of constraints %d", [[self.view constraints] count]);
    [self.view removeConstraints:[self.view constraints]];
    NSLog(@"amout of constraints %d", [[self.view constraints] count]);
    if ((orientation == UIInterfaceOrientationLandscapeLeft) || (orientation == UIInterfaceOrientationLandscapeRight)) {
        [self setLandscapeConstraints];
    } else {
        [self setPortraitConstraints];
    }
}

- (void) fetchCurrentAccount {
    ARLAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
    self.account =     [Account retrieveFromDbWithLocalId:[[NSUserDefaults standardUserDefaults] objectForKey:@"accountLocalId"]
                                       withManagedContext:appDelegate.managedObjectContext] ;
    
    //    self.userName.text = self.account.name;
    //    UIImage * image = [UIImage imageWithData:self.account.picture];
    //    self.imageView.image = image;
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) loginClicked {
    
    if (self.account) {
        ARLAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
        [ARLAccountDelegator deleteCurrentAccount:appDelegate.managedObjectContext];
        self.account = nil;
        [self createViewsProgrammatically];
    } else {
        ARLOauthViewController *controller = [[ARLOauthViewController alloc] init];
        
        [self presentViewController:controller animated:TRUE completion:nil];
    }
}

- (void) myRunsClicked {
    UINavigationController * monitorMenuViewController = [self.storyboard instantiateViewControllerWithIdentifier:@"myRunsID"];
    [self presentViewController:monitorMenuViewController animated:NO completion:nil];
    
}

@end
