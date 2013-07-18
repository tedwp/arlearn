//
//  ARLAnswerItemView.h
//  ARLearn
//
//  Created by Stefaan Ternier on 7/12/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ARLAnswerItemView : UIView {
    BOOL selected;
}

@property (nonatomic, readwrite) BOOL selected;

@property (strong, nonatomic) UIButton * selectedButton;
//@property (strong, nonatomic) UISwitch * uiSwitch;
@property (strong, nonatomic) UILabel * uiLabel;
@property (strong, nonatomic) NSString * identifier;

-(void) setText:(NSString*) text;
- (BOOL) isSelected;
@end
