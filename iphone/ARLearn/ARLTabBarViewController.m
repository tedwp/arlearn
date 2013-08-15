//
//  ARLTabBarViewController.m
//  ARLearn
//
//  Created by Stefaan Ternier on 8/14/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLTabBarViewController.h"

@interface ARLTabBarViewController ()

@end

@implementation ARLTabBarViewController

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
    self.backButon.title = NSLocalizedString(@"back", nil);
    
}\

- (void) viewDidAppear:(BOOL)animated {
    for (UIViewController* controller in self.viewControllers) {
        if (controller.tabBarItem.title) {
            controller.tabBarItem.title = NSLocalizedString(controller.tabBarItem.title, nil);
        }
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
