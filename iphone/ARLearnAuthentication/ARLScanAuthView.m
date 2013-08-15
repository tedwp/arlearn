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
        self.scanLabel.backgroundColor = [UIColor colorWithRed:0.0 green:0.0 blue:0.0 alpha:0.0];
        
        
        self.scanLabel.text = NSLocalizedString(@"ScanToken", nil);
        [self addSubview:self.scanLabel];
        
        self.scanImage = [UIButton buttonWithType:UIButtonTypeCustom];
        self.scanImage.translatesAutoresizingMaskIntoConstraints = NO;
        [self.scanImage addTarget:self action:@selector(scanClick) forControlEvents:UIControlEventTouchUpInside];
        [self.scanImage setImage:[UIImage imageNamed:@"qrscanner.png"] forState:UIControlStateNormal];
        
        [self.scanImage imageView].contentMode = UIViewContentModeScaleAspectFit;
        [self addSubview:self.scanImage];
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
    
    [self addConstraints:[NSLayoutConstraint
                          constraintsWithVisualFormat:@"V:|-[scanImage]-|"
                          options:nil
                          metrics:nil
                          views:viewsDictionary]];
    
}

- (void) scanClick {
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
    
}



@end
