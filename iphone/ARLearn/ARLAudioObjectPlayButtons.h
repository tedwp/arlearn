//
//  ARLAudioObjectPlayButtons.h
//  ARLearn
//
//  Created by Stefaan Ternier on 7/11/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ARLAudioObjectPlayer.h"
@class ARLAudioObjectPlayer;




@interface ARLAudioObjectPlayButtons : UIView 
@property (weak, nonatomic)  ARLAudioObjectPlayer * player;
@property (strong, nonatomic) UIButton * playButton;
@property (strong, nonatomic) UIButton * stopButton;


- (id) init ;

//- (void) playing;
//- (void) stopPlaying ;
- (void) setButtonsAccordingToStatus;
@end
