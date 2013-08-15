//
//  ARLAudioRecordButtons.h
//  ARLearn
//
//  Created by Stefaan Ternier on 8/9/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ARLAudioRecorder.h"
@class ARLAudioRecorder;

@interface ARLAudioRecordButtons : UIView

@property (weak, nonatomic)  ARLAudioRecorder * recorder;
@property (nonatomic, strong) UIButton * leftButton;
@property (nonatomic, strong) UIButton * rightButton;

- (id) init ;
- (void) setButtonsAccordingToStatus;

@end
