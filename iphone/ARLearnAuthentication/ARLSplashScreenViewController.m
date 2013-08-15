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

-(BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    return (interfaceOrientation == UIInterfaceOrientationPortrait) || (interfaceOrientation == UIInterfaceOrientationPortraitUpsideDown);
//     return (interfaceOrientation == UIInterfaceOrientationPortrait) ;
}

-(NSUInteger)supportedInterfaceOrientations
{
    return UIInterfaceOrientationMaskPortrait;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    [self.loggedInView.layer setCornerRadius:10.0f];
    [self.loggedInView.layer setBorderColor:[UIColor lightGrayColor].CGColor];
    [self.loggedInView.layer setBorderWidth:1.5f];
    
    
    //    [self createLoggedInImage]
    [self addGradient];
}

- (void) addGradient {
    UIColor *darkOp = [UIColor colorWithRed:(99.0f/256.0f) green:(187.0f/256.0f) blue:(255.0f/256.0f)alpha:0.5];
    UIColor *lightOp = [UIColor colorWithRed:1.0f green:1.0f blue:1.0f alpha:0.01];
    
    // Create the gradient
    CAGradientLayer *gradient = [CAGradientLayer layer];
    
    // Set colors
    gradient.colors = [NSArray arrayWithObjects:
                       (id)lightOp.CGColor,
                       (id)darkOp.CGColor,
                       nil];
    
    // Set bounds
    gradient.frame = self.view.bounds;
    
    // Add the gradient to the view
    [self.view.layer insertSublayer:gradient atIndex:0];
}

- (void) viewWillAppear:(BOOL)animated {
    [self fetchCurrentAccount];
    [self createViewsProgrammatically];
    [self.loggedInView setAccount:self.account];
}


- (void) createViewsProgrammatically {
    [self createLoginButton];
    [self createARLearnImage];
    if (self.account) {
        [self createLoggedInView];
        [self createMyRunsButton];
        [self createGamesButton];
    } else {
        [self.loggedInView removeFromSuperview];
        [self.myRunsButton removeFromSuperview];
        [self.gamesButton removeFromSuperview];
        self.loggedInView = nil;
        self.myRunsButton = nil;
        self.gamesButton = nil;
    }
    
    if ((self.interfaceOrientation == UIInterfaceOrientationLandscapeLeft) || (self.interfaceOrientation == UIInterfaceOrientationLandscapeRight)) {
        
        [self setPortraitConstraints];
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
        self.arlearnImage = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"arlearn_logo_background.png"]];
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
    [self.myRunsButton setTitle:NSLocalizedString(@"MyRuns", nil) forState:UIControlStateNormal];
}

- (void) createGamesButton {
    if (!self.gamesButton) {
        self.gamesButton = [UIButton buttonWithType:UIButtonTypeRoundedRect];
        self.gamesButton.translatesAutoresizingMaskIntoConstraints = NO;
        [self.gamesButton addTarget:self action:@selector(gamesClicked) forControlEvents:UIControlEventTouchUpInside];
        [self.view addSubview:self.gamesButton];
    }
    [self.gamesButton setTitle:NSLocalizedString(@"MyGames", nil) forState:UIControlStateNormal];
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
              self.gamesButton, @"gamesButton",
         self.loggedInView, @"loggedInView",
         self.arlearnImage, @"arlearnImage", nil];
        verticalContstraint = @"V:|-[loggedInView(==100)]-[arlearnImage(==150)]-(>=10)-[loginButton]-[myRunsButton]-[gamesButton]-|";
        
        [self.view addConstraints:[NSLayoutConstraint
                                   constraintsWithVisualFormat:@"H:|-[myRunsButton]-|"
                                   options:NSLayoutFormatDirectionLeadingToTrailing
                                   metrics:nil
                                   views:viewsDictionary]];
        [self.view addConstraints:[NSLayoutConstraint
                                   constraintsWithVisualFormat:@"H:|-[gamesButton]-|"
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
        verticalContstraint = @"V:|-[arlearnImage(==150)]-(>=10)-[loginButton(==40)]-|";
        
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
     self.gamesButton, @"gamesButton",
     self.loginButton, @"loginButton",
     self.loggedInView, @"loggedInView",
     self.arlearnImage, @"arlearnImage", nil];
    NSString* verticalContstraint1 = @"V:|-[loggedInView(==80)]-(>=20)-[loginButton]-[myRunsButton]-[gamesButton]|";
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

    [self.view addConstraints:[NSLayoutConstraint
                               constraintsWithVisualFormat:@"H:|-[arlearnImage(==200)]-[gamesButton]-|"
                               options:NSLayoutFormatDirectionLeadingToTrailing
                               metrics:nil
                               views:viewsDictionary]];
}


-(void)willRotateToInterfaceOrientation: (UIInterfaceOrientation)orientation duration:(NSTimeInterval)duration {
    [self.view removeConstraints:[self.view constraints]];
    if ((orientation == UIInterfaceOrientationLandscapeLeft) || (orientation == UIInterfaceOrientationLandscapeRight)) {
        [self setPortraitConstraints];
    } else {
        [self setPortraitConstraints];
    }
}

- (void) fetchCurrentAccount {
    ARLAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
    self.account =     [Account retrieveFromDbWithLocalId:[[NSUserDefaults standardUserDefaults] objectForKey:@"accountLocalId"]
                                       withManagedContext:appDelegate.managedObjectContext] ;

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

- (void) gamesClicked {
    UINavigationController * monitorMenuViewController = [self.storyboard instantiateViewControllerWithIdentifier:@"gameLibrary"];
//    [monitorMenuViewController.navigationController setNavigationBarHidden:YES animated:YES];
    [self presentViewController:monitorMenuViewController animated:NO completion:nil];
    
}

@end
