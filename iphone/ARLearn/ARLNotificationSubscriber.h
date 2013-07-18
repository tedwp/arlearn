//
//  ARLNotificationSubscriber.h
//  ARLearn
//
//  Created by Stefaan Ternier on 1/28/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ARLCloudSynchronizer.h"
#import "ARLAppDelegate.h"
@protocol NotificationHandler <NSObject>
@required
- (void) onNotification : (NSDictionary*) notification;
@end

@interface ARLNotificationSubscriber : NSObject

- (void) dispatchMessage: (NSDictionary *) message;

- (void) addNotificationHandler: (NSString *) notificationType handler:(id <NotificationHandler>) notificationHandler;

+ (ARLNotificationSubscriber *) sharedSingleton;

@end
