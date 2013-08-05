//
//  ARLTestingRunViewController.m
//  ARLearn
//
//  Created by Stefaan Ternier on 1/26/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLTestingRunViewController.h"

@interface ARLTestingRunViewController ()

@end

@implementation ARLTestingRunViewController
@synthesize resultPane;
@synthesize runIdField;



- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad{
    [super viewDidLoad];
}

- (void)didReceiveMemoryWarning{
    [super didReceiveMemoryWarning];
}




- (void) networkReturned {
    
}

- (void) networkReturned : (ARLBean*) bean {
    self.resultPane.text = [bean description];
}

- (void) noNetwork {
    
}

- (void) networkReturnedJson : (NSDictionary*) dict {
    NSLog(@"receive items %@", dict);
}


- (IBAction)allRuns:(id)sender {
    NSDictionary * runs = [ARLNetwork runsParticipate ];
    self.resultPane.text = [runs description];
}

- (IBAction)withRunId:(id)sender {
    NSDictionary * runs = [ARLNetwork runsWithId:[NSNumber numberWithLongLong:[self.runIdField.text longLongValue]]];
    self.resultPane.text = [runs description];
//    [[[ARLNetworkClientRuns alloc] init] getRun:[self.runIdField.text longLongValue] delegate:self];
//    [[[ARLNetworkClientGeneralItemsVisibility alloc] init] getVisibleItems:[self.runIdField.text longLongValue] account:@"arlearn1" del:self];
}
@end
