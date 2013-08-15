//
//  ARLVideoViewController.m
//  ARLearn
//
//  Created by Stefaan Ternier on 7/5/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLVideoViewController.h"


@interface ARLVideoViewController ()

@end

@implementation ARLVideoViewController

@synthesize generalItem = _generalItem;
@synthesize run = _run;
@synthesize headerText;

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
    
    NSDictionary * jsonDict = [NSKeyedUnarchiver unarchiveObjectWithData:self.generalItem.json];
    
    self.dataCollectionWidget = [[ARLDataCollectionWidget alloc] init:[jsonDict objectForKey:@"openQuestion"] viewController:self];
    if (self.dataCollectionWidget.isVisible) {
        self.dataCollectionWidget.run = self.run;
        self.dataCollectionWidget.generalItem = self.generalItem;
    }
    [self createPlayButton];
    [self setConstraints];
    [self prepareTempVideoUrl];
}

- (void) createPlayButton {
    
    self.playButton = [UIButton buttonWithType:UIButtonTypeRoundedRect];
    [self.playButton addTarget:self action:@selector(playVideo) forControlEvents:UIControlEventTouchUpInside];
    self.playButton.translatesAutoresizingMaskIntoConstraints = NO;
    UIImage * image = [UIImage imageNamed:@"black_play.png"];
    [self.playButton setBackgroundImage:image forState:UIControlStateNormal];
    [self.view addSubview:self.playButton];
}

- (void) setConstraints {
    ARLDataCollectionWidget* widget = self.dataCollectionWidget;
    [self.view addSubview:self.dataCollectionWidget];
    
    NSDictionary *viewsDictionary;
    if (widget) {
        viewsDictionary =
        [[NSDictionary alloc] initWithObjectsAndKeys:
         self.webView, @"webView",
         self.playButton, @"playButton",
         widget, @"widget", nil];
    } else {
        viewsDictionary =
        [[NSDictionary alloc] initWithObjectsAndKeys:
         self.webView, @"webView",
         self.playButton, @"playButton", nil];
    }
    
    NSString* verticalContstraint;
    if (widget.isVisible) {
        verticalContstraint = @"V:|-(>=100)-[playButton(==70)][widget(==80)]|";
    } else {
        NSLog(@"widget nt vis");
        verticalContstraint = @"V:|-(>=100)-[playButton(==70)]|";
    }
    
    [self.view addConstraints:[NSLayoutConstraint
                               constraintsWithVisualFormat:verticalContstraint
                               options:NSLayoutFormatDirectionLeadingToTrailing
                               metrics:nil
                               views:viewsDictionary]];
    [self.view addConstraints:[NSLayoutConstraint
                               constraintsWithVisualFormat:@"V:|[webView(>=100)]|"
                               options:NSLayoutFormatDirectionLeadingToTrailing
                               metrics:nil
                               views:viewsDictionary]];
    
    [self.view addConstraints:[NSLayoutConstraint
                               constraintsWithVisualFormat:@"H:|-(>=50)-[playButton(==50)]-(>=50)-|"
                               options:NSLayoutFormatDirectionLeadingToTrailing
                               metrics:nil
                               views:viewsDictionary]];
    [self.view addConstraints:[NSLayoutConstraint
                               constraintsWithVisualFormat:@"H:|[webView]|"
                               options:NSLayoutFormatDirectionLeadingToTrailing
                               metrics:nil
                               views:viewsDictionary]];
    
    NSLayoutConstraint *center = [NSLayoutConstraint
                                  constraintWithItem:self.playButton attribute:NSLayoutAttributeCenterX
                                  relatedBy:NSLayoutRelationEqual
                                  toItem:self.view attribute:NSLayoutAttributeCenterX
                                  multiplier:1 constant:0];
    [self.view addConstraint:center];
    
    if (widget.isVisible) {
        [self.view addConstraints:[NSLayoutConstraint
                                   constraintsWithVisualFormat:@"H:|[widget]|"
                                   options:NSLayoutFormatDirectionLeadingToTrailing
                                   metrics:nil
                                   views:NSDictionaryOfVariableBindings(widget)]];
    }
    
    
}

- (void) prepareTempVideoUrl {
    NSData *mediaData;
    NSSet *dataSet = self.generalItem.data;
    for(GeneralItemData * data in dataSet) {
        NSLog(@"nsdata %@", data.name);
        mediaData = data.data;
    }
    if (!mediaData) return;
    NSString* path = [NSSearchPathForDirectoriesInDomains(NSCachesDirectory,
                                                          NSUserDomainMask,
                                                          YES) lastObject];
    NSString* tmpFile = [path stringByAppendingPathComponent:@"temp.mp4"];
    NSLog(@"my path %@", tmpFile);
    //your data
    //    NSString *movePath=[[NSBundle mainBundle] pathForResource:tmpFile ofType:@"mp4"];
    [mediaData writeToFile:tmpFile atomically:YES];
    self.tempVideoUrl = [NSURL fileURLWithPath:tmpFile];
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)playVideo{
    self.moviePlayerController = [[MPMoviePlayerController alloc] initWithContentURL:self.tempVideoUrl];
    [self.view addSubview:self.moviePlayerController.view];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(moviePlayBackDidFinish:)
                                                 name:MPMoviePlayerPlaybackDidFinishNotification
                                               object:self.moviePlayerController];
    self.moviePlayerController.fullscreen = YES;
    AVAudioSession *audioSession = [AVAudioSession sharedInstance];
    [audioSession setCategory:AVAudioSessionCategoryPlayAndRecord error:NULL];
    
    self.moviePlayerController.movieSourceType = MPMovieSourceTypeFile;
    self.moviePlayerController.controlStyle = MPMovieControlStyleDefault;
    self.moviePlayerController.shouldAutoplay = YES;
    [self.moviePlayerController prepareToPlay];
    
    if ([self.moviePlayerController respondsToSelector:@selector(loadState)]){
        [[NSNotificationCenter defaultCenter] addObserver:self
                                                 selector:@selector(moviePlayerLoadStateChanged:)
                                                     name:MPMoviePlayerLoadStateDidChangeNotification
                                                   object:self.moviePlayerController];
    }
    
    
    [self.moviePlayerController play];
    
}

- (void) moviePlayerLoadStateChanged:(NSNotification*)notification{
    
}



- (void) moviePlayBackDidFinish:(NSNotification*)notification {
    AVAudioSession *audioSession = [AVAudioSession sharedInstance];
    [audioSession setActive:NO error:nil];
    
    MPMoviePlayerController *player = [notification object];
    
    [[NSNotificationCenter defaultCenter] removeObserver:self
                                                    name:MPMoviePlayerPlaybackDidFinishNotification
                                                  object:player];
    
    if ([player
         respondsToSelector:@selector(setFullscreen:animated:)])
    {
        [player.view removeFromSuperview];
    }
}


@end
