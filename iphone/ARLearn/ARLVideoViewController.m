//
//  ARLVideoViewController.m
//  ARLearn
//
//  Created by Stefaan Ternier on 7/5/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLVideoViewController.h"
#import <MediaPlayer/MediaPlayer.h>

@interface ARLVideoViewController ()

@end

@implementation ARLVideoViewController

@synthesize generalItem = _generalItem;
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
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)playVideo:(id)sender {
     NSData *mediaData;
    NSSet *dataSet = self.generalItem.data;
    for(GeneralItemData * data in dataSet) {
        NSLog(@"nsdata %@", data.name);
        mediaData = data.data;
    }
    NSString* path = [NSSearchPathForDirectoriesInDomains(NSCachesDirectory,
                                                          NSUserDomainMask,
                                                          YES) lastObject];
    NSString* tmpFile = [path stringByAppendingPathComponent:@"temp.mp4"];
    NSLog(@"my path %@", tmpFile);
    //your data
//    NSString *movePath=[[NSBundle mainBundle] pathForResource:tmpFile ofType:@"mp4"];
    [mediaData writeToFile:tmpFile atomically:YES];
    NSURL *moveUrl= [NSURL fileURLWithPath:tmpFile];
//    MPMoviePlayerController *movePlayer=[[MPMoviePlayerController alloc]init];
//    [movePlayer setContentURL:moveUrl];
//    [movePlayer play];
//
//    NSString *filepath   =   @"http://dl.dropboxusercontent.com/u/1571790/Najib%20Amhali%20-%20Freefight%20-%20Broodje%20Shoarma-1.mp4";
//    NSURL    *fileURL    =   [NSURL fileURLWithPath:filepath];
    MPMoviePlayerController *moviePlayerController = [[MPMoviePlayerController alloc] initWithContentURL:moveUrl];
    [self.view addSubview:moviePlayerController.view];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(moviePlayBackDidFinish:)
                                                 name:MPMoviePlayerPlaybackDidFinishNotification
                                               object:moviePlayerController];
    moviePlayerController.fullscreen = YES;
    moviePlayerController.movieSourceType = MPMovieSourceTypeFile;
//    moviePlayerController.initialPlaybackTime = -1.0;
    moviePlayerController.controlStyle = MPMovieControlStyleDefault;
    moviePlayerController.shouldAutoplay = YES;
    [moviePlayerController prepareToPlay];

    if ([moviePlayerController respondsToSelector:@selector(loadState)]){
        [[NSNotificationCenter defaultCenter] addObserver:self
                                                 selector:@selector(moviePlayerLoadStateChanged:)
                                                     name:MPMoviePlayerLoadStateDidChangeNotification
                                                   object:moviePlayerController];
    }
    

    [moviePlayerController play];

}

- (void) moviePlayerLoadStateChanged:(NSNotification*)notification
{
    NSLog(@"my path %@", [notification description]);

}



- (void) moviePlayBackDidFinish:(NSNotification*)notification {
    
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
