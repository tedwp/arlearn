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
    [self.loggedInView.layer setCornerRadius:10.0f];
    
    // border
    [self.loggedInView.layer setBorderColor:[UIColor lightGrayColor].CGColor];
    [self.loggedInView.layer setBorderWidth:1.5f];
    
//    // drop shadow
//    [self.loggedInView.layer setShadowColor:[UIColor blackColor].CGColor];
//    [self.loggedInView.layer setShadowOpacity:0.8];
//    [self.loggedInView.layer setShadowRadius:1.0];
//    [self.loggedInView.layer setShadowOffset:CGSizeMake(2.0, 2.0)];
}

- (void) viewWillAppear:(BOOL)animated {
    
    NSLog(@"view will app");
//    [self useDocument];
    [self fetchCurrentAccount];
    
}

//-(void) useDocument {
//    ARLAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
//    
//    if (![[NSFileManager defaultManager] fileExistsAtPath:[appDelegate.arlearnDatabase.fileURL path]]) {
//        [appDelegate.arlearnDatabase saveToURL:appDelegate.arlearnDatabase.fileURL forSaveOperation:UIDocumentSaveForCreating completionHandler:^(BOOL succes) {
//        [self fetchCurrentAccount];
//        }];
//    } else if (appDelegate.arlearnDatabase.documentState == UIDocumentStateClosed) {
//        [appDelegate.arlearnDatabase openWithCompletionHandler:^(BOOL succes) {
//        [self fetchCurrentAccount];
//        }];
//    } else if (appDelegate.arlearnDatabase.documentState == UIDocumentStateNormal) {
//        [self fetchCurrentAccount];
//    }
//}

- (void) fetchCurrentAccount {
    ARLAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];

    NSLog(@"localid %@",     [[NSUserDefaults standardUserDefaults] objectForKey:@"accountLocalId"]);
    Account * account =     [Account retrieveFromDbWithLocalId:[[NSUserDefaults standardUserDefaults] objectForKey:@"accountLocalId"]
                                         withManagedContext:appDelegate.managedObjectContext] ;
    NSLog(@"localid %@",    account.name );
    self.userName.text = account.name;
    UIImage * image = [UIImage imageWithData:account.picture];
    self.imageView.image = image;

}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
