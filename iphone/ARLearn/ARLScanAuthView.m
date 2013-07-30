//
//  ARLScanAuthView.m
//  ARLearn
//
//  Created by Stefaan Ternier on 7/25/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLScanAuthView.h"

@implementation ARLScanAuthView

@synthesize scanLabel;
@synthesize viewController;


- (id)init {
    self = [super init];
    if (self) {
        self.backgroundColor = [UIColor clearColor];
        self.translatesAutoresizingMaskIntoConstraints = NO;
        [self.layer setCornerRadius:10.0f];
        [self.layer setBorderColor:[UIColor lightGrayColor].CGColor];
        [self.layer setBorderWidth:1.5f];
        
        self.scanLabel = [[UILabel alloc] init];
        self.scanLabel.translatesAutoresizingMaskIntoConstraints = NO;
        self.scanLabel.numberOfLines = 0;
        self.scanLabel.adjustsFontSizeToFitWidth=NO;
        self.scanLabel.lineBreakMode=NSLineBreakByWordWrapping;
        self.scanLabel.font=[UIFont fontWithName:@"Helvetica" size:13.0];

        self.scanLabel.text = NSLocalizedString(@"ScanToken", nil);
//        self.scanLabel.text = @"Scan an ARLearn token to authenticate";
        [self addSubview:self.scanLabel];

//        self.labelSeparator = [[UIView alloc] init];
//        self.labelSeparator.translatesAutoresizingMaskIntoConstraints = NO;
//        self.labelSeparator.backgroundColor = [UIColor grayColor];
//        [self addSubview:self.labelSeparator];
        

        //
        self.scanImage = [UIButton buttonWithType:UIButtonTypeCustom];
        self.scanImage.translatesAutoresizingMaskIntoConstraints = NO;
        [self.scanImage addTarget:self action:@selector(scanClick) forControlEvents:UIControlEventTouchUpInside];
        [self.scanImage setImage:[UIImage imageNamed:@"qrscanner.png"] forState:UIControlStateNormal];

        [self.scanImage imageView].contentMode = UIViewContentModeScaleAspectFit;
        [self addSubview:self.scanImage];

        
//        self.scanImage = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"qrscanner.png"]];
//        self.scanImage.translatesAutoresizingMaskIntoConstraints = NO;
//        self.scanImage.contentMode = UIViewContentModeScaleAspectFit;
//        [self addSubview:self.scanImage];
        
        [self setConstraints];
    }
    return self;
}

- (void) setConstraints {
    
    NSDictionary * viewsDictionary =
    [[NSDictionary alloc] initWithObjectsAndKeys:
     self.scanImage, @"scanImage",


     self.scanLabel, @"scanLabel", nil];
    
    NSString* verticalContstraint = @"H:|-[scanImage(==80)]-[scanLabel]-|";
    
    [self addConstraints:[NSLayoutConstraint
                               constraintsWithVisualFormat:verticalContstraint
                               options:NSLayoutFormatDirectionLeadingToTrailing
                               metrics:nil
                               views:viewsDictionary]];
    [self addConstraints:[NSLayoutConstraint
                               constraintsWithVisualFormat:@"V:|-[scanLabel]-|"
                               options:nil
                               metrics:nil
                               views:viewsDictionary]];
    
//     [self addConstraint:[NSLayoutConstraint
//                                  constraintWithItem:self.labelSeparator attribute:NSLayoutAttributeCenterY
//                                  relatedBy:NSLayoutRelationEqual
//                                  toItem:self attribute:NSLayoutAttributeCenterY
//                                  multiplier:1 constant:0]];
//    
//    [self addConstraints:[NSLayoutConstraint
//                          constraintsWithVisualFormat:@"V:|-[labelSeparator(==50)]"
//                          options:nil
//                          metrics:nil
//                          views:viewsDictionary]];
    [self addConstraints:[NSLayoutConstraint
                          constraintsWithVisualFormat:@"V:|-[scanImage]-|"
                          options:nil
                          metrics:nil
                          views:viewsDictionary]];
    
}

- (void) scanClick {
//    NSLog(@"clicked scan button");
//}
//
//
//- (IBAction)qrScan:(id)sender {
    
    
    ZBarReaderViewController *reader = [ZBarReaderViewController new];
    reader.readerDelegate =  self.viewController;
    reader.supportedOrientationsMask = ZBarOrientationMaskAll;
    
    ZBarImageScanner *scanner = reader.scanner;
    
    [scanner setSymbology: ZBAR_I25
                   config: ZBAR_CFG_ENABLE
                       to: 0];
    
    [self.viewController presentViewController:reader animated:YES completion:nil];
}


- (void) imagePickerController: (UIImagePickerController*) reader didFinishPickingMediaWithInfo: (NSDictionary*) info
{
    // ADD: get the decode results
    id<NSFastEnumeration> results =
    [info objectForKey: ZBarReaderControllerResults];
    ZBarSymbol *symbol = nil;
    for(symbol in results)
        break;
    NSLog(@"scanned %@", symbol.data);
    
    //    [reader dismissModalViewControllerAnimated: YES];
    
//    NSDictionary * auth = [ARLNetwork anonymousLogin:symbol.data];
//    if ([auth objectForKey:@"error"]){
//    } else {
//        NSString * authString = [auth objectForKey:@"auth"];
//        [[NSUserDefaults standardUserDefaults] setObject:authString forKey:@"auth"];
//        NSDictionary *accountDetails = [ARLNetwork accountDetails];
//        ARLAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
//        [Account accountWithDictionary:accountDetails inManagedObjectContext:appDelegate.managedObjectContext];
//        [ARLAccountDelegator resetAccount:appDelegate.managedObjectContext];
//        [self.presentingViewController dismissViewControllerAnimated:YES completion:nil];
//    }
    
}



@end
