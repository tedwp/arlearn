//
//  ARLTestingActionViewController.m
//  ARLearn
//
//  Created by Stefaan Ternier on 2/1/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLTestingActionViewController.h"

@interface ARLTestingActionViewController ()

@end

@implementation ARLTestingActionViewController

@synthesize action;
@synthesize runId;
@synthesize user;
@synthesize giType;

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

- (IBAction)submitAction:(id)sender {
    long runIdLong =     [runId.text longLongValue];
    NSDictionary *actionDict = [[NSDictionary alloc] initWithObjectsAndKeys:
                                action.text, @"action",
                                [NSNumber numberWithLong:runIdLong], @"runId",
                                user.text, @"userEmail",
                                giType.text, @"generalItemType",
                                nil];
    NSLog(@"actionDcit %@", actionDict);
    [[[ARLNetworkActions alloc] init] publishAction:actionDict];
}

- (IBAction)submitReadAction:(id)sender {
    long runIdLong = [runId.text longLongValue];
//    [[[ARLNetworkActions alloc] init] publishReadAction:runIdLong itemType:giType.text];
}

@end
