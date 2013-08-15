//
//  ARLScanObjectViewController.m
//  ARLearn
//
//  Created by Stefaan Ternier on 7/9/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLScanObjectViewController.h"
#import "ARLNetwork.h"

@interface ARLScanObjectViewController ()

@end

@implementation ARLScanObjectViewController

@synthesize generalItem = _generalItem;
@synthesize run = _run;

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
    self.headerText.title = self.generalItem.name;
    
    self.webView = [[UIWebView alloc] init];
    [self.webView loadHTMLString:self.generalItem.richText baseURL:nil];
    self.webView.translatesAutoresizingMaskIntoConstraints = NO;
    [self.view addSubview:self.webView];
    
    [self createPlayButton];
    
    [self setConstraints];

}

- (void) createPlayButton {
    
    self.scannerButton = [UIButton buttonWithType:UIButtonTypeRoundedRect];
    [self.scannerButton addTarget:self action:@selector(scanTagButton) forControlEvents:UIControlEventTouchUpInside];
    self.scannerButton.translatesAutoresizingMaskIntoConstraints = NO;
    UIImage * image = [UIImage imageNamed:@"qrscanner.png"];
    [self.scannerButton setBackgroundImage:image forState:UIControlStateNormal];
    [self.view addSubview:self.scannerButton];
}

- (void) setConstraints {
    NSDictionary *viewsDictionary = [[NSDictionary alloc] initWithObjectsAndKeys:
                                     self.webView, @"webView",
                                     self.scannerButton, @"scannerButton", nil];
    
    [self.view addConstraints:[NSLayoutConstraint
                               constraintsWithVisualFormat:@"V:|-[webView(>=100)]-[scannerButton]-|"
                               options:NSLayoutFormatDirectionLeadingToTrailing
                               metrics:nil
                               views:viewsDictionary]];
    [self.view addConstraints:[NSLayoutConstraint
                               constraintsWithVisualFormat:@"H:|[webView]|"
                               options:NSLayoutFormatDirectionLeadingToTrailing
                               metrics:nil
                               views:viewsDictionary]];
    
    [self.view addConstraint:[NSLayoutConstraint
                              constraintWithItem:self.scannerButton attribute:NSLayoutAttributeCenterX
                              relatedBy:NSLayoutRelationEqual
                              toItem:self.view attribute:NSLayoutAttributeCenterX
                              multiplier:1 constant:0]];
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)scanTagButton {
    
    ZBarReaderViewController *reader = [ZBarReaderViewController new];
    reader.readerDelegate = self;
    reader.supportedOrientationsMask = ZBarOrientationMaskAll;
    
    ZBarImageScanner *scanner = reader.scanner;
    // TODO: (optional) additional reader configuration here
    
    // EXAMPLE: disable rarely used I2/5 to improve performance
    [scanner setSymbology: ZBAR_I25
                   config: ZBAR_CFG_ENABLE
                       to: 0];
    
    // present and release the controller
    [self presentViewController:reader animated:YES completion:nil];

}

- (void) imagePickerController: (UIImagePickerController*) reader
 didFinishPickingMediaWithInfo: (NSDictionary*) info
{
    // ADD: get the decode results
    id<NSFastEnumeration> results =
    [info objectForKey: ZBarReaderControllerResults];
    ZBarSymbol *symbol = nil;
    for(symbol in results)
        break;
    
    [Action initAction:symbol.data forRun:self.run forGeneralItem:self.generalItem inManagedObjectContext:self.generalItem.managedObjectContext];
    [ARLCloudSynchronizer syncActions:self.generalItem.managedObjectContext];
    [reader dismissViewControllerAnimated:YES completion:nil];
}


@end
