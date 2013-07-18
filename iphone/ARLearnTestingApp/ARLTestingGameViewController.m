//
//  ARLTestingGameViewController.m
//  ARLearn
//
//  Created by Stefaan Ternier on 2/3/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLTestingGameViewController.h"

@interface ARLTestingGameViewController ()

@end

@implementation ARLTestingGameViewController

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

- (IBAction)allParticipateGames:(id)sender {
    NSDictionary * runs = [ARLNetwork gamesParticipate];
    self.resultPane.text = [runs description];
}

@end
