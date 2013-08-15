//
//  ARLAudioRecorder.m
//  ARLearn
//
//  Created by Stefaan Ternier on 8/5/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLAudioRecorder.h"

@implementation ARLAudioRecorder




- (void) startRecording {
    
    if (!self.tmpFileUrl) {
        NSArray *dirPaths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
        NSString *docsDir = [dirPaths objectAtIndex:0];
        self.tmpFileUrl = [NSURL fileURLWithPath:[docsDir stringByAppendingPathComponent:@"tmp.m4a"]];
        self.controller.saveButton.hidden = NO;
        [self.controller.saveButton addTarget:self action:@selector(clickedSaveButton) forControlEvents:UIControlEventTouchUpInside];

    }
    NSDictionary *recordSettings = [NSDictionary dictionaryWithObjectsAndKeys:
                                    [NSNumber numberWithInt: kAudioFormatMPEG4AAC], AVFormatIDKey,
                                    [NSNumber numberWithFloat:16000.0], AVSampleRateKey,
                                    [NSNumber numberWithInt: 1], AVNumberOfChannelsKey,
                                    nil];
    NSError *error = nil;
    self.recorder = [[AVAudioRecorder alloc] initWithURL:self.tmpFileUrl settings:recordSettings error:&error];
    [self.recorder prepareToRecord];
    
    AVAudioSession *session = [AVAudioSession sharedInstance];
    [session setCategory:AVAudioSessionCategoryPlayAndRecord error:nil];
    [session setActive:YES error:nil];
    
    [self.recorder record];
    [self.recorder setDelegate:self];
    [self startRecordTimer];
}

- (void) clickedSaveButton {
    if (self.recorder.isRecording) {
        [self.recorder stop];
    }
    if (self.player.isPlaying) {
        [self.player stop];
    }
    [self.playTimer invalidate];
    [self.recordTimer invalidate];
    [self.controller clickedSaveButton:[NSData dataWithContentsOfURL:self.tmpFileUrl]];
}

- (void) stopRecording {
    [self.recorder stop];
}

- (void) resumeRecording {
    [self.recorder record];
}

- (void) pauzeRecording {
    [self.recorder pause];
}

- (void) initAudioPlayer {
    [self.recordTimer invalidate];
    [self.controller.countField setFont:[UIFont fontWithName:@"Arial" size:50]];
    self.controller.countField.textColor = [UIColor blackColor];
    NSError * error;
    self.player = [[AVAudioPlayer alloc] initWithContentsOfURL:self.tmpFileUrl error:&error];
    if (error) {
        NSLog(@"error %@",error);
    }
}

- (void) playAudio {
    self.player.numberOfLoops = 0;
    self.player.delegate = self;
	if (self.player == nil)
		NSLog(@"error");
	else {
        [self.player play];
        [self startPlayTimer];
    }
    
}

- (void) pauzePlaying {
    if (self.player == nil)
		NSLog(@"error");
	else {
        [self.player pause];
    }
}

- (void) resumePlaying {
    if (self.player == nil)
		NSLog(@"error");
	else {
        [self.player play];
    }
}

- (void) stopPlaying {
    [self.playTimer invalidate];
    if (self.player == nil)
		NSLog(@"error");
	else {
        [self.player stop];
        self.player.currentTime =0 ;
        self.controller.countField.text = [self intervalToLabelForString:self.player.duration];
        [self.controller.countField setFont:[UIFont fontWithName:@"Arial" size:50]];
        self.controller.countField.textColor = [UIColor blackColor];
        [self.playTimer invalidate];
    }
}

- (void) startRecordTimer {
    self.controller.countField.text = @"00:00";
    [self.controller.countField setFont:[UIFont fontWithName:@"Arial-BoldMT" size:50]];
    self.controller.countField.textColor = [UIColor grayColor];
    self.recordTimer = [NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(updateLabel) userInfo:nil repeats:YES];  //this is nstimer to initiate update method
    
}

- (void) startPlayTimer {
    self.controller.countField.text = @"00:00";
    [self.controller.countField setFont:[UIFont fontWithName:@"Arial-BoldMT" size:50]];
    self.controller.countField.textColor = [UIColor grayColor];
     self.playTimer = [NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(updatePlayLabel) userInfo:nil repeats:YES];  //this is nstimer to initiate update method
     
     }
     
     -(void) updateLabel {
         
         if([self.recorder isRecording]) {
             self.controller.countField.text = [self intervalToLabelForString:self.recorder.currentTime];
         }
         
     }
     
     -(void) updatePlayLabel {
         
         if([self.player isPlaying]) {
             self.controller.countField.text = [self intervalToLabelForString:self.player.currentTime];
         }
         
     }
     
     - (NSString *) intervalToLabelForString: (double) currentTime {
         float minutes = floor(currentTime/60);
         float seconds = currentTime - (minutes * 60);
         
         NSString* minutesString = [NSString stringWithFormat:@"%0.0f", minutes];
         NSString* secondsString = [NSString stringWithFormat:@"%0.0f", seconds];
         if (secondsString.length == 1) {
             secondsString = [NSString stringWithFormat:@"0%@", secondsString];
         }
         if (minutesString.length == 1) {
             minutesString = [NSString stringWithFormat:@"0%@", minutesString];
         }
         return [[NSString alloc]
                 initWithFormat:@"%@:%@",
                 minutesString, secondsString];
     }
     
     - (void) clickedLeftButton {
         switch (self.status) {
             case ReadyToRecordPlay:
                 self.status = Recording;
                 [self startRecording];
                 [self.buttons setButtonsAccordingToStatus];
                 break;
             case ReadyToRecordPlayWithFile:
                 self.status = Recording;
                 [self startRecording];
                 [self.buttons setButtonsAccordingToStatus];
                 break;
             case Recording:
                 self.status = RecordingPauze;
                 [self pauzeRecording];
                 [self.buttons setButtonsAccordingToStatus];
                 break;
             case RecordingPauze:
                 self.status = Recording;
                 [self resumeRecording];
                 [self.buttons setButtonsAccordingToStatus];
                 break;
             case Playing:
                 self.status = ReadyToRecordPlayWithFile;
                 [self stopPlaying];
                 [self.buttons setButtonsAccordingToStatus];
                 break;
             case PlayingPauze:
                 self.status = ReadyToRecordPlayWithFile;
                 [self stopPlaying];
                 [self.buttons setButtonsAccordingToStatus];
                 break;
         }
         
         
     }
     - (void) clickedRightButton {
         switch (self.status) {
             case ReadyToRecordPlay:
                 self.status = ReadyToRecordPlay;
                 //DO nothing
                 [self.buttons setButtonsAccordingToStatus];
                 break;
             case ReadyToRecordPlayWithFile:
                 self.status = Playing;
                 [self playAudio];
                 [self.buttons setButtonsAccordingToStatus];
                 break;
             case Recording:
                 self.status = ReadyToRecordPlayWithFile;
                 [self stopRecording];
                 [self initAudioPlayer];
                 
                 [self.buttons setButtonsAccordingToStatus];
                 break;
             case RecordingPauze:
                 self.status = ReadyToRecordPlayWithFile;
                 [self stopRecording];
                 [self initAudioPlayer];
                 
                 [self.buttons setButtonsAccordingToStatus];
                 break;
             case Playing:
                 self.status = PlayingPauze;
                 [self pauzePlaying];
                 [self.buttons setButtonsAccordingToStatus];
                 break;
             case PlayingPauze:
                 self.status = Playing;
                 [self resumePlaying];
                 [self.buttons setButtonsAccordingToStatus];
                 break;
         }
         
     }
     
     - (void)audioPlayerDidFinishPlaying:(AVAudioPlayer *)player successfully:(BOOL)flag
    {
        self.status = ReadyToRecordPlayWithFile;
        [self.buttons setButtonsAccordingToStatus];
        [self.controller.countField setFont:[UIFont fontWithName:@"Arial" size:50]];
        self.controller.countField.textColor = [UIColor blackColor];
         [self.playTimer invalidate];
         
         }
         
         
         

         
         @end
