//
//  ARLAudioObjectPlayer.m
//  ARLearn
//
//  Created by Stefaan Ternier on 7/11/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLAudioObjectPlayer.h"

@implementation ARLAudioObjectPlayer

@synthesize status = _status;
@synthesize audioData = _audioData;
@synthesize player = _player;
@synthesize buttons = _buttons;

- (id) init: (NSData *) audioData {
    
    if (self = [super init]) {
        _audioData = audioData;
        _player =  [[AVAudioPlayer alloc] initWithData:audioData error:nil];
        
    }
    return self;
}

- (void) clickedPlay {
    switch (self.status) {
        case ReadyToPlayStatus:
            if (!self.player) {
                UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Audio file not yet downloaded" delegate:self cancelButtonTitle:nil otherButtonTitles:@"OK", nil];
                [alert show];
            } else {
                [self startPlayingAfterStop];
                self.status = PlayingStatus;
                [self.buttons setButtonsAccordingToStatus];
            }
            break;
        case PlayingStatus:
            [self pauzePlaying];
            self.status = PausedStatus;
            [self.buttons setButtonsAccordingToStatus];
            break;
        case PausedStatus:
            [self startPlayingAfterStop];
            self.status = PlayingStatus;
            [self.buttons setButtonsAccordingToStatus];
            break;
    }
}

- (void) clickedStop {
    switch (self.status) {
        case ReadyToPlayStatus:
            break;
        case PlayingStatus:
            [self stopPlaying];
            self.status = ReadyToPlayStatus;
            [self.buttons setButtonsAccordingToStatus];
            break;
        case PausedStatus:
            [self stopPlaying];
            self.status = ReadyToPlayStatus;
            [self.buttons setButtonsAccordingToStatus];
            break;
    }
}

-(void) startPlayingAfterStop {
    self.player.numberOfLoops = 1;
    self.player.delegate = self;
	if (self.player == nil)
		NSLog(@"error");
	else {
        [self.player play];
    }
    
}

- (void) pauzePlaying {
    if (self.player == nil)
		NSLog(@"error");
	else {
        [self.player pause];
    }
}

- (void) stopPlaying {
    if (self.player == nil)
		NSLog(@"error");
	else {
        [self.player stop];
    }
}

- (void)audioPlayerDidFinishPlaying:(AVAudioPlayer *)player successfully:(BOOL)flag
{
    self.status = ReadyToPlayStatus;
    [self.buttons setButtonsAccordingToStatus];
}
@end
