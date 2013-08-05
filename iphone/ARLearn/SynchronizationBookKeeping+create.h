//
//  SynchronizationBookKeeping+create.h
//  ARLearn
//
//  Created by Stefaan Ternier on 2/4/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "SynchronizationBookKeeping.h"

@interface SynchronizationBookKeeping (create)

+ (NSNumber*) getLastSynchronizationDate : (NSManagedObjectContext *) context type:(NSString *) type;
+ (NSNumber*) getLastSynchronizationDate : (NSManagedObjectContext *) managedContext type:(NSString *) type context:(NSNumber *) identifierContext;

+ (SynchronizationBookKeeping *) createEntry: (NSString *) type
                                        time:(NSNumber *) time
                      inManagedObjectContext: (NSManagedObjectContext * ) context ;

+ (SynchronizationBookKeeping *) createEntry: (NSString *) type
                                        time: (NSNumber *) time
                                   idContext: (NSNumber *) idContext
                      inManagedObjectContext: (NSManagedObjectContext * ) context;
@end
