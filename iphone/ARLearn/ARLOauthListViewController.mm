//
//  ARLOauthListViewController.m
//  ARLearn
//
//  Created by Stefaan Ternier on 6/24/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLOauthListViewController.h"
#import "Account+create.h"
#import "ARLAppDelegate.h"
#import "ARLAccountDelegator.h"

@interface ARLOauthListViewController ()

@end

@implementation ARLOauthListViewController

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
    if (self.dismiss) {
        NSLog(@"now destroy myself");
        //        [self dismissViewControllerAnimated:YES completion:nil];
        [self.presentingViewController dismissViewControllerAnimated:YES completion:nil];
    }
}

- (void) viewWillAppear:(BOOL)animated {
    
    NSLog(@"view will app");
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)facebookButton:(id)sender {
        [self.presentingViewController dismissViewControllerAnimated:YES completion:nil];
    
}

- (IBAction)googleButton:(id)sender {
    ARLOauthWebViewController* svc = [self.storyboard instantiateViewControllerWithIdentifier:@"oauthWebView"];
    [svc setModalTransitionStyle:UIModalTransitionStyleCoverVertical];
    [self presentViewController:svc animated:YES completion:nil];
//    svc.oauthList = self;
    [svc loadAuthenticateUrl:@"https://accounts.google.com/o/oauth2/auth?redirect_uri=http://ar-learn.appspot.com/oauth/google&response_type=code&client_id=594104153413-7ec2cn8jkuh7b4j27dn6ohjqst929s8h.apps.googleusercontent.com&approval_prompt=force&scope=profile+email" delegate:svc];


}

- (IBAction)qrScan:(id)sender {
    
    NSLog(@"clicked scan");
    ZXingWidgetController *widController =
    [[ZXingWidgetController alloc] initWithDelegate:self showCancel:YES OneDMode:NO];
    
    NSMutableSet *readers = [[NSMutableSet alloc ] init];
    
    MultiFormatReader* reader = [[MultiFormatReader alloc] init];
    [readers addObject:reader];
    
    
    widController.readers = readers;
    //    [readers release];
    
    NSBundle *mainBundle = [NSBundle mainBundle];
    widController.soundToPlay =
    [NSURL fileURLWithPath:[mainBundle pathForResource:@"beep-beep" ofType:@"aiff"] isDirectory:NO];
    
    [self presentModalViewController:widController animated:YES];

    // [self presentViewController:widController animated:YES completion:nil];
    
    //    [widController release];

}



- (void)zxingController:(ZXingWidgetController*)controller didScanResult:(NSString *)result {
    NSLog(@"code %@", result);
    NSDictionary * auth = [ARLNetwork anonymousLogin:result];
    NSLog(@"auth %@", auth);
    if ([auth objectForKey:@"error"]){
    NSLog(@"error ");
    } else {
        NSString * authString = [auth objectForKey:@"auth"];
        [[NSUserDefaults standardUserDefaults] setObject:authString forKey:@"auth"];
        NSLog(@"auth %@", authString);
        NSDictionary *accountDetails = [ARLNetwork accountDetails];
            ARLAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
        [Account accountWithDictionary:accountDetails inManagedObjectContext:appDelegate.arlearnDatabase.managedObjectContext];
        [ARLAccountDelegator resetAccount:appDelegate.arlearnDatabase.managedObjectContext];
        [self.presentingViewController dismissViewControllerAnimated:YES completion:nil];
    }
//        [self dismissModalViewControllerAnimated:NO];

//    [self.presentingViewController.presentingViewController dismissViewControllerAnimated:YES completion:nil];

}

- (void)zxingControllerDidCancel:(ZXingWidgetController*)controller {
    [self dismissModalViewControllerAnimated:NO];
}


-(void) disappear {
     NSLog(@"about to disappear");
}
@end
