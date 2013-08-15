//
//  ARLNotificationPlayer.m
//  ARLearn
//
//  Created by Stefaan Ternier on 8/5/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLNotificationPlayer.h"

@implementation ARLNotificationPlayer

@synthesize audioPlayer = _audioPlayer;

+ (ARLNotificationPlayer *)sharedSingleton {
    static ARLNotificationPlayer *sharedSingleton;
    static dispatch_once_t predicate;
    dispatch_once(&predicate, ^{
        sharedSingleton = [[ARLNotificationPlayer alloc] init];
    });
    return sharedSingleton;
}

- (AVAudioPlayer*) audioPlayer {
    if (!_audioPlayer) {
        NSURL *url = [NSURL fileURLWithPath:[NSString stringWithFormat:@"%@/notification.mp3", [[NSBundle mainBundle] resourcePath]]];
        NSError *error;
        _audioPlayer = [[AVAudioPlayer alloc] initWithContentsOfURL:url error:&error];
        _audioPlayer.numberOfLoops = 0;
        _audioPlayer.delegate = self;
        AVAudioSession *audioSession = [AVAudioSession sharedInstance];
        [audioSession setCategory:AVAudioSessionCategoryPlayAndRecord error:NULL];
        if (error) {
            NSLog(@"error %@",error);
        }
    }
    return _audioPlayer;
}

- (void) playNotification {
    
    [self.audioPlayer play];
    
}

- (void)audioPlayerDidFinishPlaying:(AVAudioPlayer *)player successfully:(BOOL)flag{
}

@end
