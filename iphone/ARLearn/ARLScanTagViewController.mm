//
//  ARLScanTagViewController.m
//  ARLearn
//
//  Created by Stefaan Ternier on 1/17/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLScanTagViewController.h"


@interface ARLScanTagViewController ()

@end

@implementation ARLScanTagViewController
//@synthesize id = _id;
//@synthesize moc = _moc;
@synthesize generalItem;


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

- (IBAction)scanTag:(id)sender {
   // ZXingWidgetController *widController = [[ZXingWidgetController alloc] initWithDelegate:self showCancel:YES OneDMode:NO];

}
@end
