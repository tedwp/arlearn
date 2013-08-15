//
//  ARLAudioRecordButtons.m
//  ARLearn
//
//  Created by Stefaan Ternier on 8/9/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLAudioRecordButtons.h"

@implementation ARLAudioRecordButtons

- (id) init {
    if (self = [super init]) {
        self.backgroundColor = [UIColor clearColor]; //clearColor
        self.translatesAutoresizingMaskIntoConstraints = NO;
        
        self.leftButton = [UIButton buttonWithType:UIButtonTypeRoundedRect];
        [self.leftButton addTarget:self action:@selector(clickedLeftButton) forControlEvents:UIControlEventTouchUpInside];
        self.leftButton.translatesAutoresizingMaskIntoConstraints = NO;
        [self addSubview:self.leftButton];
        
        self.rightButton = [UIButton buttonWithType:UIButtonTypeRoundedRect];
        [self.rightButton addTarget:self action:@selector(clickedRightButton) forControlEvents:UIControlEventTouchUpInside];
        self.rightButton.translatesAutoresizingMaskIntoConstraints = NO;
        
        [self  addSubview:self.rightButton];
        
        
        NSDictionary *viewsDictionary =
        [[NSDictionary alloc] initWithObjectsAndKeys:
         self.leftButton, @"leftButton",
         self.rightButton, @"rightButton", nil];
        
        [self addConstraints:[NSLayoutConstraint
                              constraintsWithVisualFormat:@"V:|-0-[leftButton(==70)]"
                              options:0
                              metrics:nil
                              views:viewsDictionary]];
        [self addConstraints:[NSLayoutConstraint
                              constraintsWithVisualFormat:@"V:|-0-[rightButton(==70)]"
                              options:0
                              metrics:nil
                              views:viewsDictionary]];
        [self addConstraints:[NSLayoutConstraint
                              constraintsWithVisualFormat:@"H:|-(>=80)-[leftButton(==70)]-(==10)-[rightButton(==70)]-(>=80)-|"
                              options:0
                              metrics:nil
                              views:viewsDictionary]];
    }
    return self;
}

- (void) clickedLeftButton {
    [self.recorder clickedLeftButton];
    
}
- (void) clickedRightButton {
    [self.recorder clickedRightButton];
}

- (void) setButtonsAccordingToStatus {
    switch (self.recorder.status) {
        case ReadyToRecordPlay:
            [self setButtons:@"button_rec" rightImage:@"grey_play"];
            //            [self setButtons:@"black_play.png" stopImage:@"grey_stop.png"];
            break;
        case ReadyToRecordPlayWithFile:
            [self setButtons:@"button_rec" rightImage:@"black_play"];            
            break;
        case Recording:
            [self setButtons:@"black_pause" rightImage:@"black_stop"];            
            break;
        case RecordingPauze:
            [self setButtons:@"button_rec" rightImage:@"black_stop"];
            break;
        case Playing:
            [self setButtons:@"black_stop" rightImage:@"black_pause"];
            break;
            
        case PlayingPauze:
            [self setButtons:@"black_stop" rightImage:@"black_play"];            
            break;

    }
    
}

-(void) setButtons: (NSString *) left rightImage:(NSString *) right {
    UIImage * leftUIImage = [UIImage imageNamed:left];
    UIImage * rightUIImage = [UIImage imageNamed:right];
    
    [self.leftButton setBackgroundImage:leftUIImage forState:UIControlStateNormal];
    [self.rightButton setBackgroundImage:rightUIImage forState:UIControlStateNormal];
}


@end
