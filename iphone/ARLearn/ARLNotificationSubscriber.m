//
//  ARLNotificationSubscriber.m
//  ARLearn
//
//  Created by Stefaan Ternier on 1/28/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLNotificationSubscriber.h"


@implementation ARLNotificationSubscriber {
    NSMutableDictionary * notDict;
}

+ (ARLNotificationSubscriber *)sharedSingleton {
   static ARLNotificationSubscriber *sharedSingleton;
    static dispatch_once_t predicate;
    dispatch_once(&predicate, ^{
        sharedSingleton = [[ARLNotificationSubscriber alloc] init];
    });
    return sharedSingleton;
}

- (id) init {
    self = [super init];
    notDict = [[NSMutableDictionary alloc] init];
    return self;
}

- (void) registerAccount: (NSString* ) fullId {
    NSString * deviceUniqueIdentifier = [[NSUserDefaults standardUserDefaults] objectForKey:@"deviceUniqueIdentifier"];
    NSString * deviceToken = [[NSUserDefaults standardUserDefaults] objectForKey:@"deviceToken"];
    [ARLNetwork registerDevice:deviceToken withUID:deviceUniqueIdentifier withAccount:fullId];
}

- (void) dispatchMessage: (NSDictionary *) message {
    message = [message objectForKey:@"aps"];
    if ([@"org.celstec.arlearn2.beans.run.User" isEqualToString:[message objectForKey:@"type"]]) {
        ARLCloudSynchronizer* synchronizer = [[ARLCloudSynchronizer alloc] init];
        ARLAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
        [synchronizer createContext:appDelegate.managedObjectContext];
        synchronizer.syncRuns = YES;
        synchronizer.syncGames = YES;
        [synchronizer sync];
    }
    if ([@"org.celstec.arlearn2.beans.notification.RunModification" isEqualToString:[message objectForKey:@"type"]]) {
        NSLog(@"about to update runs %@", [[message objectForKey:@"run"] objectForKey:@"runId"]);
        ARLCloudSynchronizer* synchronizer = [[ARLCloudSynchronizer alloc] init];
        synchronizer.syncRuns = YES;
        [synchronizer sync];
        
    }
    if ([@"org.celstec.arlearn2.beans.notification.GeneralItemModification" isEqualToString:[message objectForKey:@"type"]]) {
        NSLog(@"about to update gi %@", [message objectForKey:@"itemId"] );
        
        ARLCloudSynchronizer* synchronizer = [[ARLCloudSynchronizer alloc] init];
        ARLAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
        [synchronizer createContext:appDelegate.managedObjectContext];
        
        synchronizer.gameId = [NSDecimalNumber decimalNumberWithString:[message objectForKey:@"gameId"]];
        synchronizer.visibilityRunId = [NSDecimalNumber decimalNumberWithString:[message objectForKey:@"runId"]];
        [synchronizer sync];
    }
    
    NSMutableSet *set = [notDict objectForKey:[message objectForKey:@"type"]];
    for (id <NotificationHandler> listener in set) {
        [listener onNotification:message];
    }
}

- (void) addNotificationHandler: (NSString *) notificationType handler:(id <NotificationHandler>) notificationHandler {
    if (![notDict valueForKey:notificationType]) {
        [notDict setObject:[[NSMutableSet alloc] init] forKey:notificationType];
    }
    [[notDict valueForKey:notificationType] addObject:notificationHandler];
}



@end
