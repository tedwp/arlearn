//
//  ARLAudioRecorder.h
//  ARLearn
//
//  Created by Stefaan Ternier on 8/5/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <AVFoundation/AVFoundation.h>
#import "Response+Create.h"
#import "GeneralItem+ARLearnBeanCreate.h"
#import "Run+ARLearnBeanCreate.h"
#import "ARLCloudSynchronizer.h"
#import "ARLAudioRecordButtons.h"
#import "ARLAudioRecorderViewController.h"
@class ARLAudioRecordButtons;
@class ARLAudioRecorderViewController;

typedef NS_ENUM(NSInteger, RecorderState) {
    ReadyToRecordPlay,
    ReadyToRecordPlayWithFile,
    Recording,
    RecordingPauze,
    Playing,
    PlayingPauze
};

@interface ARLAudioRecorder : NSObject <AVAudioRecorderDelegate, AVAudioPlayerDelegate>

@property (nonatomic, readwrite) RecorderState status;


@property (nonatomic, strong) AVAudioRecorder *recorder;
@property (strong, nonatomic ) AVAudioPlayer * player;
@property (strong, nonatomic ) NSTimer * recordTimer;
@property (strong, nonatomic ) NSTimer * playTimer;
@property (weak, nonatomic ) ARLAudioRecorderViewController * controller;

@property (nonatomic, strong) NSURL *tmpFileUrl;
@property (strong, nonatomic ) ARLAudioRecordButtons * buttons;


- (void) clickedLeftButton;
- (void) clickedRightButton;


@end
