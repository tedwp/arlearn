//
//  ARLAnswerItemView.m
//  ARLearn
//
//  Created by Stefaan Ternier on 7/12/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLAnswerItemView.h"

@implementation ARLAnswerItemView

//@synthesize uiSwitch = _uiSwitch;
@synthesize uiLabel = _uiLabel;
@synthesize identifier = _identifier;
@synthesize selectedButton = _selectedButton;
@synthesize selected = _selected;

- (id)init{
    self = [super init];
    if (self) {
        self.backgroundColor = [UIColor clearColor]; //clearColor
        self.translatesAutoresizingMaskIntoConstraints = NO;
        
        _uiLabel = [[UILabel alloc] init];
        _uiLabel.translatesAutoresizingMaskIntoConstraints = NO;
        _uiLabel.text = @"testLabel";
        
        
        _selectedButton = [UIButton buttonWithType:UIButtonTypeRoundedRect];
        [_selectedButton addTarget:self action:@selector(tapSelectButton) forControlEvents:UIControlEventTouchUpInside];
        _selectedButton.translatesAutoresizingMaskIntoConstraints = NO;
        UIImage * image = [UIImage imageNamed:@"cb_green_off.png"];
        _selected = NO;
        [_selectedButton setBackgroundImage:image forState:UIControlStateNormal];
        
        [self addSubview:_selectedButton];
        [self addSubview:_uiLabel];
        
        NSDictionary* viewsDictionary = [[NSDictionary alloc] initWithObjectsAndKeys: _selectedButton, @"selectedButton", _uiLabel, @"uiLabel", nil];
        
        [self addConstraints:[NSLayoutConstraint
                                   constraintsWithVisualFormat:@"V:|-4-[selectedButton(==22)]-3-|"
                                   options:NSLayoutFormatDirectionLeadingToTrailing
                                   metrics:nil
                                   views:viewsDictionary]];
        
        
        [self addConstraints:[NSLayoutConstraint
                              constraintsWithVisualFormat:@"V:|[uiLabel(==29)]|"
                              options:NSLayoutFormatDirectionLeadingToTrailing
                              metrics:nil
                              views:viewsDictionary]];
        
        [self addConstraints:[NSLayoutConstraint
                                   constraintsWithVisualFormat:@"H:|-2-[selectedButton(==22)]-[uiLabel(>=100)]|"
                                   options:NSLayoutFormatDirectionLeadingToTrailing
                                   metrics:nil
                                   views:viewsDictionary]];

        
    }
    return self;
}

- (void) tapSelectButton {
    if (self.selected) {
        self.selected = NO;
        UIImage * image = [UIImage imageNamed:@"cb_green_off.png"];
        [_selectedButton setBackgroundImage:image forState:UIControlStateNormal];

    } else {
        self.selected = YES;
        UIImage * image = [UIImage imageNamed:@"cb_green_on.png"];
        [_selectedButton setBackgroundImage:image forState:UIControlStateNormal];
    }
}


-(void) setText:(NSString*) text {
    self.uiLabel.text = text;
}

- (BOOL) isSelected {
    return self.selected;
}

@end
