//
//  SynchronizationBookKeeping+create.m
//  ARLearn
//
//  Created by Stefaan Ternier on 2/4/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "SynchronizationBookKeeping+create.h"

@implementation SynchronizationBookKeeping (create)


+ (NSNumber*) getLastSynchronizationDate : (NSManagedObjectContext *) context type:(NSString *) type{
    return [self getLastSynchronizationDate:context type:type context:nil];
}

+ (NSNumber*) getLastSynchronizationDate : (NSManagedObjectContext *) managedContext type:(NSString *) type context:(NSNumber *) identifierContext {
//    NSString * key = [NSString stringWithFormat:@"%@+%@", type, identifierContext];
//    SynchronizationBookKeeping * objectFromCache = [[ARLCloudSynchronizer syncDates] objectForKey:key];
//    if (objectFromCache) {
//        return objectFromCache.lastSynchronization;
//    }
    NSFetchRequest *fetch = [NSFetchRequest fetchRequestWithEntityName:@"SynchronizationBookKeeping"];
    if (identifierContext) {
        fetch.predicate = [NSPredicate predicateWithFormat: @"type = %@ and context = %lld", type, [identifierContext longLongValue]];
    } else {
        fetch.predicate = [NSPredicate predicateWithFormat: @"type = %@ ", type];
    }
    NSError *error;
    NSArray *result = [managedContext executeFetchRequest:fetch error:&error];
    if (!result) {
        NSLog(@"%@", [error localizedDescription]);
        
    }
    if ([result count] == 0) {
        return [NSNumber numberWithInt:0];
    } else {
        SynchronizationBookKeeping * bookKeeping = [result lastObject];
//        [[ARLCloudSynchronizer syncDates] setObject:bookKeeping forKey:key];
        return bookKeeping.lastSynchronization;
    }
}

+ (SynchronizationBookKeeping *) createEntry: (NSString *) type
                                        time: (NSNumber *) time
                      inManagedObjectContext: (NSManagedObjectContext * ) context {

    return [self createEntry:type time:time idContext:nil inManagedObjectContext:context];
}

+ (SynchronizationBookKeeping *) createEntry: (NSString *) type
                                        time: (NSNumber *) time
                                   idContext: (NSNumber *) idContext
                      inManagedObjectContext: (NSManagedObjectContext * ) context {
    
    SynchronizationBookKeeping * bkItem = nil;
    
    NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"SynchronizationBookKeeping"];
    if (idContext) {
        request.predicate = [NSPredicate predicateWithFormat: @"type = %@ and context = %lld", type, [idContext longLongValue]];
    } else {
        request.predicate = [NSPredicate predicateWithFormat: @"type = %@ ", type];
    }
    
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
     if (idContext)   bkItem.context = idContext;
    bkItem.lastSynchronization = time;
   
    return bkItem;
}

@end
