//
//  ARLAudioObjectPlayButtons.m
//  ARLearn
//
//  Created by Stefaan Ternier on 7/11/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLAudioObjectPlayButtons.h"

@implementation ARLAudioObjectPlayButtons

@synthesize player = _player;

- (id) init {
    if (self = [super init]) {
        self.backgroundColor = [UIColor clearColor]; //clearColor
        self.translatesAutoresizingMaskIntoConstraints = NO;  
        
        NSString * playImage = @"black_play.png";
        self.playButton = [self addButtonWithImage:playImage action:@selector(clickedPlay) ];
        [self addSubview:self.playButton];
        
        NSString * stopImage = @"grey_stop.png";
        self.stopButton = [self addButtonWithImage:stopImage action:@selector(clickedStop) ];
        [self addSubview:self.stopButton];
        
        NSDictionary *viewsDictionary =
        [[NSDictionary alloc] initWithObjectsAndKeys:
         self.stopButton, @"stopButton",
         self.playButton, @"playButton", nil];
        
        [self addConstraints:[NSLayoutConstraint
                              constraintsWithVisualFormat:@"V:|-0-[playButton(==70)]"
                              options:0
                              metrics:nil
                              views:viewsDictionary]];
        [self addConstraints:[NSLayoutConstraint
                              constraintsWithVisualFormat:@"V:|-0-[stopButton(==70)]"
                              options:0
                              metrics:nil
                              views:viewsDictionary]];
        [self addConstraints:[NSLayoutConstraint
                              constraintsWithVisualFormat:@"H:|-(>=80)-[playButton(==70)]-(==10)-[stopButton(==70)]-(>=80)-|"
                              options:0
                              metrics:nil
                              views:viewsDictionary]];
    }
    return self;
}

- (UIButton *) addButtonWithImage:(NSString *)imageString action:(SEL)selector {
    UIButton *button = [UIButton buttonWithType:UIButtonTypeRoundedRect];
    [button addTarget:self action:selector forControlEvents:UIControlEventTouchUpInside];
    button.translatesAutoresizingMaskIntoConstraints = NO;
    UIImage * image = [UIImage imageNamed:imageString];
    
    [button setBackgroundImage:image forState:UIControlStateNormal];
    return button;
}

- (void) clickedPlay {
    [self.player clickedPlay];
}

- (void) clickedStop {
    [self.player clickedStop];
}

- (void) setButtonsAccordingToStatus {
    switch (self.player.status) {
        case ReadyToPlayStatus:
            [self setButtons:@"black_play.png" stopImage:@"grey_stop.png"];
            break;
        case PlayingStatus:
            [self setButtons:@"black_pause.png" stopImage:@"black_stop.png"];
            break;
        case PausedStatus:
            [self setButtons:@"black_play.png" stopImage:@"black_stop.png"];
            break;
            
    }
    
}

-(void) setButtons: (NSString *) playImage stopImage:(NSString *) stopImage {
    UIImage * playUIImage = [UIImage imageNamed:playImage];
    UIImage * stopUIImage = [UIImage imageNamed:stopImage];
    
    [self.playButton setBackgroundImage:playUIImage forState:UIControlStateNormal];
    [self.stopButton setBackgroundImage:stopUIImage forState:UIControlStateNormal];
}

@end
