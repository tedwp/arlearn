//
//  ARLYoutubeViewController.m
//  ARLearn
//
//  Created by Stefaan Ternier on 8/6/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLYoutubeViewController.h"

@implementation ARLYoutubeViewController



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
    
    self.youtubePlay = [UIButton buttonWithType:UIButtonTypeRoundedRect];
    [self.youtubePlay addTarget:self action:@selector(playYoutubeMovie) forControlEvents:UIControlEventTouchUpInside];
    self.youtubePlay.translatesAutoresizingMaskIntoConstraints = NO;
    UIImage * image = [UIImage imageNamed:@"black_play"];
//        UIImage *newImage = [UIImage imageWithData: [NSData dataWithContentsOfURL: [NSURL URLWithString:@"http://halgatewood.com/youtube/i/tqa5vkY6Bjo.jpg"]]];
//    if (newImage) {
//        image = newImage;
//    }
    [self.youtubePlay setBackgroundImage:image forState:UIControlStateNormal];
    [self.view addSubview:self.youtubePlay];
        NSDictionary * jsonDict = [NSKeyedUnarchiver unarchiveObjectWithData:self.generalItem.json];
    NSString* youtubeUrl = [jsonDict objectForKey:@"youtubeUrl"];
    if ([youtubeUrl hasPrefix:@"http://www.youtube.com/watch?v="]) {
        youtubeUrl = [youtubeUrl stringByReplacingOccurrencesOfString:@"http://www.youtube.com/watch?v="
                                             withString:@"http://halgatewood.com/youtube/i/"];
        youtubeUrl =[NSString stringWithFormat:@"%@%@", youtubeUrl,  @".jpg"];
    }
    dispatch_queue_t backgroundQueue = dispatch_queue_create("downloadimagebutton", nil);
    dispatch_async(backgroundQueue, ^(void) {
        UIImage *newImage = [UIImage imageWithData: [NSData dataWithContentsOfURL: [NSURL URLWithString:youtubeUrl]]];
        if (newImage) {
            dispatch_async(dispatch_get_main_queue(), ^{
                [self.youtubePlay setBackgroundImage:newImage forState:UIControlStateNormal];
                
            });
            
        }
       
    });

}

- (void) setConstraints {
    NSDictionary *viewsDictionary = [[NSDictionary alloc] initWithObjectsAndKeys:
     self.webView, @"webView",
     self.youtubePlay, @"youtubePlay", nil];
    
    [self.view addConstraints:[NSLayoutConstraint
                               constraintsWithVisualFormat:@"V:|-[webView(>=100)]-[youtubePlay]-|"
                               options:NSLayoutFormatDirectionLeadingToTrailing
                               metrics:nil
                               views:viewsDictionary]];
    [self.view addConstraints:[NSLayoutConstraint
                               constraintsWithVisualFormat:@"H:|[webView]|"
                               options:NSLayoutFormatDirectionLeadingToTrailing
                               metrics:nil
                               views:viewsDictionary]];
    
    [self.view addConstraint:[NSLayoutConstraint
                              constraintWithItem:self.youtubePlay attribute:NSLayoutAttributeCenterX
                              relatedBy:NSLayoutRelationEqual
                              toItem:self.view attribute:NSLayoutAttributeCenterX
                              multiplier:1 constant:0]];
}

- (void) playYoutubeMovie {
    NSDictionary * jsonDict = [NSKeyedUnarchiver unarchiveObjectWithData:self.generalItem.json];

    NSURL *url = [NSURL URLWithString:[jsonDict objectForKey:@"youtubeUrl"]];
    [[UIApplication sharedApplication] openURL:url];
}

@end
