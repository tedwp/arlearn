//
//  ARLFileCloudSynchronizer.m
//  ARLearn
//
//  Created by Stefaan Ternier on 7/16/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLFileCloudSynchronizer.h"

@implementation ARLFileCloudSynchronizer

@synthesize context = _context;


- (void) createContext: (NSManagedObjectContext*) mainContext {
    self.parentContext = mainContext;
    self.context = [[NSManagedObjectContext alloc] initWithConcurrencyType:NSPrivateQueueConcurrencyType];
    self.context.parentContext = mainContext;
}

- (void) sync {
    [self.context performBlock:^{
        [self asyncExecution];
    }];
    
}

//+ (id)sharedInstance
//{
//    static dispatch_once_t pred;
//    static ARLFileCloudSynchronizer *synchronizer = nil;
//    
//    dispatch_once(&pred, ^{
//        synchronizer = [[self alloc] init];
//        
//
//    });
//    return synchronizer;
//}
//
//- (id) init {
//    
//    if (self = [super init]) {
//        ARLAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
//        NSPersistentStoreCoordinator *coordinator = [appDelegate persistentStoreCoordinator];
//        self.context = [[NSManagedObjectContext alloc] init];
////        [self.context setMergePolicy:NSOverwriteMergePolicy];
//        [self.context setPersistentStoreCoordinator:coordinator];
//        queue = dispatch_queue_create("Sync background queue", NULL);
//        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(_mocDidSaveNotification_nes:) name:NSManagedObjectContextDidSaveNotification object:nil];
//        
//
//        
//    }
//    return self;
//}
//
//- (void)_mocDidSaveNotification_nes:(NSNotification *)notification
//{
//    NSManagedObjectContext *savedContext = [notification object];
//    if (self.context == savedContext) {
//        return;
//    }
//    if (self.context.persistentStoreCoordinator != savedContext.persistentStoreCoordinator){
//        return;
//    }
////    dispatch_sync(queue, ^{
//        NSLog(@"ARLFileCloud ready with merging");
//
//        [self.context mergeChangesFromContextDidSaveNotification:notification];
//        NSLog(@"ARLFileCloud ready with merging");
//
////    });
//}
//
//- (void) sync {
//    dispatch_async(queue, ^{
//        [self asyncExecution];
//    });
//    
//}

- (void) asyncExecution {
//    NSLog(@"made it here");
//    @synchronized(self){
        [self downloadGeneralItems];
//    }
}

- (void)saveContext
{
    NSError *error = nil;
    
    if (self.context) {
        if ([self.context hasChanges]){
            if (![self.context save:&error]) {
                NSLog(@"Unresolved error %@, %@", error, [error userInfo]);
                abort();
            }
            NSLog(@"save completed");
            [self.parentContext performBlock:^{
                NSError *error = nil;
                if (![self.parentContext save:&error]) {abort();}
            }];

        }
        NSLog(@"save completed");

    }
    NSLog(@"save completed");
}

- (void) downloadGeneralItems {
    for (GeneralItemData* giData in [GeneralItemData getUnsyncedData:self.context]) {
        NSLog(@"gidata url = %@ replicated = %@ error = %@ ", giData.url, giData.replicated, giData.error);
        NSURL  *url = [NSURL URLWithString:giData.url];
        NSData *urlData = [NSData dataWithContentsOfURL:url];
        if ( urlData ){
            giData.data = urlData;
            giData.replicated = [NSNumber numberWithBool:YES];
        } else {
            NSLog(@"sth went wrong");
        }
        
    [self saveContext];
    }
}


@end
