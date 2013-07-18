//
//  ARLToggleButton.h
//  ARLearn
//
//  Created by Stefaan Ternier on 7/15/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ARLToggleButton : UIButton {
    BOOL selected;
}

@property (nonatomic, readwrite) BOOL selected;

@end
