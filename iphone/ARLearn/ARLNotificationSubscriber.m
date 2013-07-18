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

+ (ARLNotificationSubscriber *)sharedSingleton
{
    static ARLNotificationSubscriber *sharedSingleton;
    
    @synchronized(self)
    {
        if (!sharedSingleton)
            sharedSingleton = [[ARLNotificationSubscriber alloc] init];
        return sharedSingleton;
    }
}

- (id) init {
    self = [super init];
    notDict = [[NSMutableDictionary alloc] init];
    return self;
}

- (void) dispatchMessage: (NSDictionary *) message {
    message = [message objectForKey:@"aps"];
    if ([@"org.celstec.arlearn2.beans.notification.RunModification" isEqualToString:[message objectForKey:@"type"]]) {
        NSLog(@"about to update runs %@", [[message objectForKey:@"run"] objectForKey:@"runId"]);
        ARLAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
        NSManagedObjectContext *moc = [appDelegate managedObjectContext];
        ARLCloudSynchronizer* synchronizer = [[ARLCloudSynchronizer alloc] init];
        synchronizer.syncRuns = YES;
        [synchronizer sync];
        
//        [ARLCloudSynchronizer syncronizeRuns:moc];
//        [ARLCloudSynchronizer syncronizeRuns];
        
    }
    if ([@"org.celstec.arlearn2.beans.notification.GeneralItemModification" isEqualToString:[message objectForKey:@"type"]]) {
        NSLog(@"about to update gi %@", [message objectForKey:@"itemId"] );
        ARLAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
        NSManagedObjectContext *moc = [appDelegate managedObjectContext];
        
        ARLCloudSynchronizer* synchronizer = [[ARLCloudSynchronizer alloc] init];
        synchronizer.gameId = [message objectForKey:@"gameId"];
        synchronizer.visibilityRunId = [message objectForKey:@"runId"];
        [synchronizer sync];
//        
//        [ARLCloudSynchronizer synchronizeGeneralItemsAndVisibilityStatments:[message objectForKey:@"runId"] withManagedContext:moc];
//
//        [ARLCloudSynchronizer synchronizeGeneralItems:[message objectForKey:@"gameId"] withManagedContext:moc];
//        [ARLCloudSynchronizer synchronizeGeneralItemVisiblityStatements:[message objectForKey:@"runId"] withManagedContext:moc];
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
