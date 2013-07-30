//
//  ARLOauthView.m
//  ARLearn
//
//  Created by Stefaan Ternier on 7/25/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLOauthView.h"

@implementation ARLOauthView

@synthesize viewController;

- (id)init {
    self = [super init];
    if (self) {
        self.backgroundColor = [UIColor clearColor];
        self.translatesAutoresizingMaskIntoConstraints = NO;
        
        [self.layer setCornerRadius:10.0f];
        [self.layer setBorderColor:[UIColor lightGrayColor].CGColor];
        [self.layer setBorderWidth:1.5f];
        
        self.authLabel = [[UILabel alloc] init];
        self.authLabel.translatesAutoresizingMaskIntoConstraints = NO;
        self.authLabel.numberOfLines = 3;
        self.authLabel.adjustsFontSizeToFitWidth=NO;
        self.authLabel.lineBreakMode=NSLineBreakByWordWrapping;
        self.authLabel.font=[UIFont fontWithName:@"Helvetica" size:13.0];
        [self.authLabel setTextAlignment:UITextAlignmentCenter];
        self.authLabel.text = @"Authenticate with existing account";
        [self addSubview:self.authLabel];
        
        self.twitterImage = [UIButton buttonWithType:UIButtonTypeCustom];
        self.twitterImage.translatesAutoresizingMaskIntoConstraints = NO;
        [self.twitterImage addTarget:self action:@selector(twitterAuth) forControlEvents:UIControlEventTouchUpInside];
        [self.twitterImage setImage:[UIImage imageNamed:@"twitter.png"] forState:UIControlStateNormal];
        [self.twitterImage imageView].contentMode = UIViewContentModeScaleAspectFit;
        [self addSubview:self.twitterImage];
        
        
        self.googleImage = [UIButton buttonWithType:UIButtonTypeCustom];
        self.googleImage.translatesAutoresizingMaskIntoConstraints = NO;
        [self.googleImage addTarget:self action:@selector(googleAuth) forControlEvents:UIControlEventTouchUpInside];
        [self.googleImage setImage:[UIImage imageNamed:@"google.png"] forState:UIControlStateNormal];
        [self.googleImage imageView].contentMode = UIViewContentModeScaleAspectFit;
        [self addSubview:self.googleImage];
        
        self.facebookImage = [UIButton buttonWithType:UIButtonTypeCustom];
        self.facebookImage.translatesAutoresizingMaskIntoConstraints = NO;
        [self.facebookImage addTarget:self action:@selector(facebookAuth) forControlEvents:UIControlEventTouchUpInside];
        [self.facebookImage setImage:[UIImage imageNamed:@"facebook.png"] forState:UIControlStateNormal];
        [self.facebookImage imageView].contentMode = UIViewContentModeScaleAspectFit;
        [self addSubview:self.facebookImage];
        
        self.linkedInImage = [UIButton buttonWithType:UIButtonTypeCustom];
        self.linkedInImage.translatesAutoresizingMaskIntoConstraints = NO;
        [self.linkedInImage addTarget:self action:@selector(linkedInAuth) forControlEvents:UIControlEventTouchUpInside];
        [self.linkedInImage setImage:[UIImage imageNamed:@"linkedin.png"] forState:UIControlStateNormal];
        [self.linkedInImage imageView].contentMode = UIViewContentModeScaleAspectFit;
        [self addSubview:self.linkedInImage];
        
        
        [self setConstraints];

    }
    return self;
}

- (void) setConstraints {
    
    NSDictionary * viewsDictionary =
    [[NSDictionary alloc] initWithObjectsAndKeys:
     self.authLabel, @"authLabel",
     self.linkedInImage, @"linkedInImage",
     self.facebookImage, @"facebookImage",
     self.googleImage, @"googleImage",
     self.twitterImage, @"twitterImage", nil];
    
    NSString* verticalContstraint1 = @"V:|-[authLabel(==40)]-[googleImage(==facebookImage)]-[facebookImage]-|";
    NSString* verticalContstraint2 = @"V:|-[authLabel(==40)]-[twitterImage(==linkedInImage)]-[linkedInImage]-|";
//    NSString* verticalContstraint3 = @"V:|-[authLabel(==40)]-[facebookImage]-|";
//    NSString* verticalContstraint4 = @"V:|-[authLabel(==40)]-[linkedInImage]-|";
    [self addConstraints:[NSLayoutConstraint
                          constraintsWithVisualFormat:verticalContstraint1
                          options:NSLayoutFormatDirectionLeadingToTrailing
                          metrics:nil
                          views:viewsDictionary]];
    [self addConstraints:[NSLayoutConstraint
                          constraintsWithVisualFormat:verticalContstraint2
                          options:NSLayoutFormatDirectionLeadingToTrailing
                          metrics:nil
                          views:viewsDictionary]];
//    [self addConstraints:[NSLayoutConstraint
//                          constraintsWithVisualFormat:verticalContstraint3
//                          options:NSLayoutFormatDirectionLeadingToTrailing
//                          metrics:nil
//                          views:viewsDictionary]];
//    [self addConstraints:[NSLayoutConstraint
//                          constraintsWithVisualFormat:verticalContstraint4
//                          options:NSLayoutFormatDirectionLeadingToTrailing
//                          metrics:nil
//                          views:viewsDictionary]];
    [self addConstraints:[NSLayoutConstraint
                          constraintsWithVisualFormat:@"H:|-[authLabel]-|"
                          options:nil
                          metrics:nil
                          views:viewsDictionary]];
//    [self addConstraints:[NSLayoutConstraint
//                          constraintsWithVisualFormat:@"H:|-[googleImage]-[twitterImage(==googleImage)]-[facebookImage(==googleImage)]-[linkedInImage(==googleImage)]-|"
//                          options:nil
//                          metrics:nil
//                          views:viewsDictionary]];
    
    [self addConstraints:[NSLayoutConstraint
                          constraintsWithVisualFormat:@"H:|-[googleImage]-[twitterImage(==googleImage)]-|"
                          options:nil
                          metrics:nil
                          views:viewsDictionary]];
    [self addConstraints:[NSLayoutConstraint
                          constraintsWithVisualFormat:@"H:|-[facebookImage(==googleImage)]-[linkedInImage(==googleImage)]-|"
                          options:nil
                          metrics:nil
                          views:viewsDictionary]];
    
}

-(void) facebookAuth {
    [self.viewController oauth:[NSNumber numberWithInt:1]];
}

-(void) googleAuth {
    [self.viewController oauth:[NSNumber numberWithInt:2]];
}

-(void) linkedInAuth {
    [self.viewController oauth:[NSNumber numberWithInt:3]];
}

-(void) twitterAuth {
    [self.viewController oauth:[NSNumber numberWithInt:4]];
}

@end
