//
//  SynchronizationBookKeeping+create.m
//  ARLearn
//
//  Created by Stefaan Ternier on 2/4/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "SynchronizationBookKeeping+create.h"

@implementation SynchronizationBookKeeping (create)

+ (SynchronizationBookKeeping *) createEntry: (NSString *) type
                                        time: (NSNumber *) time
                      inManagedObjectContext: (NSManagedObjectContext * ) context {

    SynchronizationBookKeeping * bkItem = nil;

    NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"SynchronizationBookKeeping"];
    request.predicate = [NSPredicate predicateWithFormat:@"type = %@", type];

    
    NSArray *bkItems = [context executeFetchRequest:request error:nil];
    if (!bkItems || ([bkItems count] > 1)) {
//        // handle error
    } else if (![bkItems count]) {
        bkItem = [NSEntityDescription insertNewObjectForEntityForName:@"SynchronizationBookKeeping"
                                           inManagedObjectContext:context];
        bkItem.type = type;
    } else {
        bkItem = [bkItems lastObject];

        
    }
    bkItem.lastSynchronization = time;
    NSError * error;
    if (![context save:&error]) {
        NSLog(@"error %@", error);
    }
    return bkItem;
}

+ (SynchronizationBookKeeping *) createEntry: (NSString *) type
                                        time: (NSNumber *) time
                                   idContext: (NSNumber *) idContext
                      inManagedObjectContext: (NSManagedObjectContext * ) context {
    
    SynchronizationBookKeeping * bkItem = nil;
    
    NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"SynchronizationBookKeeping"];
    request.predicate = [NSPredicate predicateWithFormat:@"type = %@", type];
    
    
    NSArray *bkItems = [context executeFetchRequest:request error:nil];
    if (!bkItems || ([bkItems count] > 1)) {
        //        // handle error
    } else if (![bkItems count]) {
        bkItem = [NSEntityDescription insertNewObjectForEntityForName:@"SynchronizationBookKeeping"
                                               inManagedObjectContext:context];
        bkItem.type = type;
        bkItem.context = idContext;
    } else {
        bkItem = [bkItems lastObject];
    }
    bkItem.lastSynchronization = time;
    NSError * error;
    if (![context save:&error]) {
        NSLog(@"error %@", error);
    }
    return bkItem;
}

@end
