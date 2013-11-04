//
//  ARLAppearDisappearDelegator.h
//  ARLearn
//
//  Created by Stefaan Ternier on 8/7/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ARLAppDelegate.h"

@interface ARLAppearDisappearDelegator : UIViewController

+ (ARLAppearDisappearDelegator *) sharedSingleton;
- (void) setTimer: (NSDate *) fireDate ;
@end
