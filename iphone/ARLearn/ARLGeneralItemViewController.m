//
//  ARLGeneralItemViewController.m
//  ARLearn
//
//  Created by Stefaan Ternier on 1/15/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLGeneralItemViewController.h"

@interface ARLGeneralItemViewController ()

@end

@implementation ARLGeneralItemViewController

@synthesize generalItem = _generalItem;
@synthesize headerText = _headerText;
@synthesize webView = _webView;
@synthesize provideActionButton;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.headerText.title = self.generalItem.name;
    NSDictionary * jsonDict = [NSKeyedUnarchiver unarchiveObjectWithData:self.generalItem.json];
    if ([jsonDict objectForKey:@"openQuestion"]) {
        self.provideActionButton.hidden = NO;
    } else {
        self.provideActionButton.hidden = YES;
    }
    [self.webView loadHTMLString:self.generalItem.richText baseURL:nil];
}

@end
