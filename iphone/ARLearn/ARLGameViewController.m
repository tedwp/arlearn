//
//  ARLGameViewController.m
//  ARLearn
//
//  Created by Stefaan Ternier on 8/13/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLGameViewController.h"

@interface ARLGameViewController ()

@end

@implementation ARLGameViewController
@synthesize game;
@synthesize gradient;


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
        [self.navigationController setNavigationBarHidden:NO animated:YES];
    self.navBar.title = self.game.title;

    [self createWebView];
    [self createPlayButton];
    [self setPortraitConstraints];
    
    [self.webView loadHTMLString:self.game.richTextDescription baseURL:nil];
    

	// Do any additional setup after loading the view.
    [self addGradient];
}

-(void)willRotateToInterfaceOrientation: (UIInterfaceOrientation)orientation duration:(NSTimeInterval)duration {
    [self.view removeConstraints:[self.view constraints]];
    if ((orientation == UIInterfaceOrientationLandscapeLeft) || (orientation == UIInterfaceOrientationLandscapeRight)) {
        [self setPortraitConstraints];

    } else {
        [self setPortraitConstraints];
    }
}
- (void)didRotateFromInterfaceOrientation:(UIInterfaceOrientation)fromInterfaceOrientation {
    [self addGradient];

}

- (void) createWebView {
    UIWebView * temp = [[UIWebView alloc] init];
    self.webView = temp;
    self.webView.translatesAutoresizingMaskIntoConstraints = NO;
    self.webView.opaque = NO;
//    self.webView.backgroundColor = [UIColor clearColor];
    self.webView.backgroundColor = [UIColor colorWithRed:1.0f green:1.0f blue:1.0f alpha:0.4];
    [self.webView.layer setCornerRadius:10.0f];
    [self.webView.layer setBorderColor:[UIColor lightGrayColor].CGColor];
    [self.webView.layer setBorderWidth:1.5f];
    [self.view addSubview:temp]; //has strong binding
}


- (void) createPlayButton {
    if (!self.playButton) {
        UIButton * tempButton =[UIButton buttonWithType:UIButtonTypeRoundedRect];
        self.playButton = tempButton;
        self.playButton.translatesAutoresizingMaskIntoConstraints = NO;
        [self.playButton addTarget:self action:@selector(playClicked) forControlEvents:UIControlEventTouchUpInside];
        [self.view addSubview:tempButton];
    }
    [self.playButton setTitle:@"Play" forState:UIControlStateNormal];
}
- (void) setPortraitConstraints {
    [self.view removeConstraints:[self.view constraints]];
 

    NSDictionary * viewsDictionary=
        [[NSDictionary alloc] initWithObjectsAndKeys:
         self.webView, @"webView",
         self.playButton, @"playButton",
         nil];
    
    
    [self.view addConstraints:[NSLayoutConstraint
                               constraintsWithVisualFormat:@"V:|-[webView(==200)]-(>=10)-[playButton(==40)]-|"
                               options:NSLayoutFormatDirectionLeadingToTrailing
                               metrics:nil
                               views:viewsDictionary]];
    [self.view addConstraints:[NSLayoutConstraint
                               constraintsWithVisualFormat:@"H:|-[webView]-|"
                               options:NSLayoutFormatDirectionLeadingToTrailing
                               metrics:nil
                               views:viewsDictionary]];
        
    [self.view addConstraints:[NSLayoutConstraint
                               constraintsWithVisualFormat:@"H:|-[playButton]-|"
                               options:NSLayoutFormatDirectionLeadingToTrailing
                               metrics:nil
                               views:viewsDictionary]];
    
}


- (void) addGradient {

    if (self.gradient) {
            gradient.frame = self.view.bounds;
    } else {
    UIColor *darkOp = [UIColor colorWithRed:(99.0f/256.0f) green:(187.0f/256.0f) blue:(255.0f/256.0f)alpha:0.5];
    UIColor *lightOp = [UIColor colorWithRed:1.0f green:1.0f blue:1.0f alpha:0.01];
    
    gradient = [CAGradientLayer layer];
    
    gradient.colors = [NSArray arrayWithObjects:
                       (id)lightOp.CGColor,
                       (id)darkOp.CGColor,
                       nil];
    
    gradient.frame = self.view.bounds;
    
    // Add the gradient to the view

    [self.view.layer insertSublayer:gradient atIndex:0];
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) playClicked {
    self.busyIndicator = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleGray];
        self.busyIndicator.translatesAutoresizingMaskIntoConstraints = NO;
//    self.busyIndicator.center = self.view.center;
//    self.busyIndicator.frame = CGRectMake(0.0, 0.0, 40.0, 40.0);
    [self.view addSubview:self.busyIndicator];
    [self.playButton setEnabled:NO];

    [self.view addConstraint:[NSLayoutConstraint
                              constraintWithItem:self.busyIndicator attribute:NSLayoutAttributeCenterX
                              relatedBy:NSLayoutRelationEqual
                              toItem:self.view attribute:NSLayoutAttributeCenterX
                              multiplier:1 constant:0]];
    [self.view addConstraint:[NSLayoutConstraint
                              constraintWithItem:self.busyIndicator attribute:NSLayoutAttributeCenterY
                              relatedBy:NSLayoutRelationEqual
                              toItem:self.view attribute:NSLayoutAttributeCenterY
                              multiplier:1 constant:0]];
    [self.busyIndicator startAnimating];
    
 
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        NSDictionary* runDict = [ARLNetwork createRun:game.gameId withTitle:game.title];
        if (runDict) {
            NSNumber* accountType = [[NSUserDefaults standardUserDefaults] objectForKey:@"accountType"];
            NSString* accountLocalId = [[NSUserDefaults standardUserDefaults] objectForKey:@"accountLocalId"];
            [ARLNetwork createUser:[runDict objectForKey:@"runId"]
                       accountType:accountType withLocalId:accountLocalId];
        }
        dispatch_async(dispatch_get_main_queue(),^ {
            [self.busyIndicator stopAnimating];
            [self endPage];

        } );
        
    });

    

}

-(void) endPage {
    [self.navigationController popViewControllerAnimated:YES];
}

@end
