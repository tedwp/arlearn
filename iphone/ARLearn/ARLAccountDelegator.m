//
//  ARLAccountDelegator.m
//  ARLearn
//
//  Created by Stefaan Ternier on 7/8/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLAccountDelegator.h"
#import "SynchronizationBookKeeping+create.h"
#import "Run+ARLearnBeanCreate.h"
#import "GeneralItemVisibility+ARLearnBeanCreate.h"
#import "Response+Create.h"

@implementation ARLAccountDelegator

+ (void) deleteCurrentAccount: (NSManagedObjectContext * ) context {
    [Account deleteAll:context];
    [self resetAccount:context];
}

+ (void) resetAccount: (NSManagedObjectContext * ) context {
    NSNumber* serverTime = [NSNumber numberWithLong:0];
    [SynchronizationBookKeeping createEntry:@"myRuns" time:serverTime inManagedObjectContext:context];
    [SynchronizationBookKeeping createEntry:@"myGames" time:serverTime inManagedObjectContext:context];
    [SynchronizationBookKeeping createEntry:@"generalItems" time:serverTime inManagedObjectContext:context];
    [SynchronizationBookKeeping createEntry:@"generalItemsVisibility" time:serverTime inManagedObjectContext:context];
    [Run deleteAllRuns:context];
    [GeneralItemVisibility deleteAll:context];
    
    [Response deleteAll:context];    
    
    [self saveChanges:context];
   
    
}



+ (void) saveChanges : (NSManagedObjectContext * ) context {
    
     NSError *error = nil;
    if (context) {
        if ([context hasChanges]){
            if (![context save:&error]) {
                NSLog(@"Unresolved error %@, %@", error, [error userInfo]);
                abort();
            }
        }
    }

}

@end
