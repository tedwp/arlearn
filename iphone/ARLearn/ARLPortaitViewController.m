//
//  ARLPortaitViewController.m
//  ARLearn
//
//  Created by Stefaan Ternier on 8/14/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLPortaitViewController.h"

@interface ARLPortaitViewController ()

@end

@implementation ARLPortaitViewController

-(NSUInteger)supportedInterfaceOrientations
{
    return UIInterfaceOrientationMaskPortrait;
}
- (void)viewDidLoad
{
    [super viewDidLoad];
  
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
