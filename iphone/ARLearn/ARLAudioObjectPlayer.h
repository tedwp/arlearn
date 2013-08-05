//
//  ARLAudioObjectPlayer.h
//  ARLearn
//
//  Created by Stefaan Ternier on 7/11/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <AVFoundation/AVAudioPlayer.h>
#import "ARLAudioObjectPlayButtons.h"
@class ARLAudioObjectPlayButtons;

typedef NS_ENUM(NSInteger, PlayingState) {
    ReadyToPlayStatus,
    PlayingStatus,
    PausedStatus
    
};

@interface ARLAudioObjectPlayer : NSObject <AVAudioPlayerDelegate> {
    PlayingState status;

}
@property (nonatomic, readwrite) PlayingState status;
@property (strong, nonatomic)  NSData* audioData;
@property (strong, nonatomic ) AVAudioPlayer * player;
@property (strong, nonatomic ) ARLAudioObjectPlayButtons * buttons;

- (id) init: (NSData *) audioData ;
- (void) clickedPlay;
- (void) clickedStop;
@end
