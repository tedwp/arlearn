//
//  ARLLoggedInView.m
//  ARLearn
//
//  Created by Stefaan Ternier on 7/25/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLLoggedInView.h"

@implementation ARLLoggedInView

@synthesize accountImage;
@synthesize loggedInAsLabel;
@synthesize accountNameLabel;


- (id)init
{
    self = [super init];
    if (self) {
        self.backgroundColor = [UIColor clearColor];
        self.translatesAutoresizingMaskIntoConstraints = NO;
        
        [self.layer setCornerRadius:10.0f];
        [self.layer setBorderColor:[UIColor lightGrayColor].CGColor];
        [self.layer setBorderWidth:1.5f];
        
        self.accountImage = [[UIImageView alloc] init];
        self.accountImage.translatesAutoresizingMaskIntoConstraints = NO;
        self.accountImage.contentMode = UIViewContentModeScaleAspectFit;
        [self addSubview:self.accountImage];
        
        self.loggedInAsLabel = [[UILabel alloc] init];
        self.loggedInAsLabel.translatesAutoresizingMaskIntoConstraints = NO;
        self.loggedInAsLabel.text =  NSLocalizedString(@"LoggedInAs", nil);
//        self.loggedInAsLabel.text = @"Logged in as";
        [self addSubview:self.loggedInAsLabel];

        self.accountNameLabel = [[UILabel alloc] init];
        self.accountNameLabel.translatesAutoresizingMaskIntoConstraints = NO;
        [self addSubview:self.accountNameLabel];
        
        [self setConstraints];
    }
    return self;
}

-(void) setAccount: (Account*) account{
    self.accountNameLabel.text = account.name;
    UIImage * image = [UIImage imageWithData:account.picture];
    self.accountImage.image = image;
}

- (void) setConstraints {
    NSDictionary * viewsDictionary =
    [[NSDictionary alloc] initWithObjectsAndKeys:
     self.accountImage, @"accountImage",
     self.loggedInAsLabel, @"loggedInAsLabel",
     self.accountNameLabel, @"accountNameLabel", nil];
    
    NSString* verticalContstraint1 = @"V:|-[loggedInAsLabel]-(>=2)-[accountNameLabel(==loggedInAsLabel)]-|";
    NSString* verticalContstraint2 = @"V:|-[accountImage]-|";
    
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
    
    [self addConstraints:[NSLayoutConstraint
                               constraintsWithVisualFormat:@"H:|-[accountImage(==50)]-[loggedInAsLabel]-|"
                               options:NSLayoutFormatDirectionLeadingToTrailing
                               metrics:nil
                               views:viewsDictionary]];
    [self addConstraints:[NSLayoutConstraint
                          constraintsWithVisualFormat:@"H:|-[accountImage(==50)]-[accountNameLabel]-|"
                          options:NSLayoutFormatDirectionLeadingToTrailing
                          metrics:nil
                          views:viewsDictionary]];
}


@end
