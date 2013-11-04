//
//  ARLNotificationPlayer.h
//  ARLearn
//
//  Created by Stefaan Ternier on 8/5/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <AVFoundation/AVFoundation.h>

@interface ARLNotificationPlayer : NSObject  <AVAudioPlayerDelegate>

@property (nonatomic, strong) AVAudioPlayer *audioPlayer;

+ (ARLNotificationPlayer *) sharedSingleton;

- (void) playNotification ;

@end
