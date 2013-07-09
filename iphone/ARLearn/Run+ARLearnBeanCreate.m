//
//  Run+ARLearnBeanCreate.m
//  ARLearn
//
//  Created by Stefaan Ternier on 2/2/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "Run+ARLearnBeanCreate.h"

@implementation Run (ARLearnBeanCreate)

+ (Run *) retrieveRun: (NSNumber *) runId inManagedObjectContext: (NSManagedObjectContext * ) context {
    Run * run = nil;
    
    NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"Run"];
    request.predicate = [NSPredicate predicateWithFormat:@"runId = %lld", [runId longLongValue]];
    NSError *error = nil;
    NSArray *runs = [context executeFetchRequest:request error:&error];
    
    if (!runs || [runs count] > 0) {
        run = [runs lastObject];
    }
    return run;

}


+ (Run *) runWithDictionary: (NSDictionary *) runDict inManagedObjectContext: (NSManagedObjectContext * ) context {
    
    Run * run = nil;
    
    NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"Run"];
    request.predicate = [NSPredicate predicateWithFormat:@"runId = %ld", [[runDict objectForKey:@"runId"] longValue]];
    NSSortDescriptor *sortDescriptor = [NSSortDescriptor sortDescriptorWithKey:@"title" ascending:YES];
    request.sortDescriptors = [NSArray arrayWithObject:sortDescriptor];
    
    NSError *error = nil;
    NSArray *runs = [context executeFetchRequest:request error:&error];
    if (!runs || ([runs count] > 1)) {
        // handle error
    } else if (![runs count]) {
        if (![[runDict objectForKey:@"deleted"] boolValue]) {
            run = [NSEntityDescription insertNewObjectForEntityForName:@"Run"
                                                inManagedObjectContext:context];
        }
        
    } else {
        run = [runs lastObject];
        
    }
    if ([[runDict objectForKey:@"deleted"] boolValue]) {
        [run.managedObjectContext deleteObject:run];
    } else {
        run.title = [runDict objectForKey:@"title"];
        run.owner = [runDict objectForKey:@"owner"];
        run.gameId = [runDict objectForKey:@"gameId"] ;
        run.runId = [runDict objectForKey:@"runId"] ;
        run.deleted = [NSNumber numberWithBool:NO];
        [self setGame:run inManagedObjectContext:context];
    }
    BOOL saved = [run.managedObjectContext save:nil];
    NSLog(@"Run successfully save %d",saved);
    
    return run;
    
    
}

+ (void) setGame: (Run *) run inManagedObjectContext: (NSManagedObjectContext * ) context {
    
    NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"Game"];
    request.predicate = [NSPredicate predicateWithFormat:@"gameId = %ld", [run.gameId longLongValue]];
   
    NSError *error = nil;
    NSArray *games = [context executeFetchRequest:request error:&error];
    if (!games || ([games count] > 1)) {
        // handle error
    } else if (![games count]) {
        
    } else {
        Game * game = [games lastObject];
        run.game = game;
    }

}

+ (void) deleteAllRuns: (NSManagedObjectContext * ) context {
    NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"Run"];
    
    NSError *error = nil;
    NSArray *runs = [context executeFetchRequest:request error:&error];
    for (id run in runs) {
        [context deleteObject:run];
    }

}

@end
