//
//  ARLCloudSynchronizer.m
//  ARLearn
//
//  Created by Stefaan Ternier on 2/4/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLCloudSynchronizer.h"

//@interface ARLCloudSynchronizer ()
//    @property (nonatomic) NSMutableDictionary *syncDates;
//@end

@implementation ARLCloudSynchronizer

//@synthesize syncDates = _syncDates;

static NSMutableDictionary *syncDates;


+ (NSMutableDictionary *) syncDates {
    if (syncDates == nil) {
        syncDates = [[NSMutableDictionary alloc] init];
    }
    return syncDates;
}

+ (void) syncronizeRuns: (NSManagedObjectContext *) context{
    [context performBlock:^{
        NSNumber * lastDate = [self getLastSynchronizationDate:context type:@"myRuns"];
        NSLog(@"about to sync run from %lld", [lastDate longLongValue]);
        dispatch_queue_t fetchQ = dispatch_queue_create("Run fetcher", NULL);
        dispatch_async(fetchQ, ^{
            NSDictionary * dict = [ARLNetwork runsParticipateFrom:lastDate];
            NSNumber * serverTime = [dict objectForKey:@"serverTime"];
            NSLog(@"allruns %@", [dict objectForKey:@"serverTime"]);
            [context performBlock:^{
                for (NSDictionary *run in [dict objectForKey:@"runs"]) {
                    [Run runWithDictionary:run inManagedObjectContext:context];
                }
                
                if (serverTime) {
                    [SynchronizationBookKeeping createEntry:@"myRuns" time:serverTime inManagedObjectContext:context];
                }
                ARLAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
                [appDelegate.arlearnDatabase saveToURL:appDelegate.arlearnDatabase.fileURL forSaveOperation:UIDocumentSaveForOverwriting completionHandler:^(BOOL success){
                    NSLog(@"database succesfully saved %d", success);
                }];
            }];
        });
        
    }];
}

+ (void) syncronizeGames: (NSManagedObjectContext *) context{
    [context performBlock:^{
        NSNumber * lastDate = [self getLastSynchronizationDate:context type:@"myGames"];
        NSLog(@"about to sync game from %lld", [lastDate longLongValue]);
        dispatch_queue_t fetchQ = dispatch_queue_create("Game fetcher", NULL);
        dispatch_async(fetchQ, ^{
            NSDictionary * gdict = [ARLNetwork gamesParticipateFrom:lastDate];
            NSNumber * serverTime = [gdict objectForKey:@"serverTime"];
            
            NSLog(@"allgames %@", [gdict objectForKey:@"serverTime"]);
            
           
            [context performBlock:^{
                for (NSDictionary *game in [gdict objectForKey:@"games"]) {
                    [Game gameWithDictionary:game inManagedObjectContext:context];
                }
                if (serverTime) {
                    [SynchronizationBookKeeping createEntry:@"myGames" time:serverTime inManagedObjectContext:context];
                }
            }];
        });
    }];
}
+ (void) synchronizeGeneralItemsWithGame: (Game *) game {
    NSManagedObjectContext * context = game.managedObjectContext;
    [context performBlock:^{
        //        NSNumber * gameId = [NSNumber numberWithLongLong:game.gameId];
        NSNumber * lastDate = [self getLastSynchronizationDate:context type:@"generalItems" context:game.gameId];
        dispatch_queue_t fetchQ = dispatch_queue_create("GeneralItems fetcher", NULL);
        dispatch_async(fetchQ, ^{
            NSDictionary * gisDict = [ARLNetwork itemsForGameFrom:game.gameId from:lastDate];
            NSNumber * serverTime = [gisDict objectForKey:@"serverTime"];
            
            NSLog(@"generalItems (gameId %@) %@", game.gameId, [gisDict objectForKey:@"serverTime"]);
            
            [context performBlock:^{
                for (NSDictionary *generalItemDict in [gisDict objectForKey:@"generalItems"]) {
                    [GeneralItem generalItemWithDictionary:generalItemDict
                                                  withGame:game
                                    inManagedObjectContext:context];
                }
                if (serverTime) {
                    [SynchronizationBookKeeping createEntry:@"generalItems"
                                                       time:serverTime
                                                  idContext:game.gameId
                                     inManagedObjectContext:context];
                }
            }];
        });
    }];
    
}


+ (void) synchronizeGeneralItemsAndVisibilityStatments: (Run *) run {
    Game * game = run.game;
    NSManagedObjectContext * context = game.managedObjectContext;
    [context performBlock:^{
        //        NSNumber * gameId = [NSNumber numberWithLongLong:game.gameId];
        NSNumber * lastDate = [self getLastSynchronizationDate:context type:@"generalItems" context:game.gameId];
        dispatch_queue_t fetchQ = dispatch_queue_create("GeneralItems fetcher", NULL);
        dispatch_async(fetchQ, ^{
            NSDictionary * gisDict = [ARLNetwork itemsForGameFrom:game.gameId from:lastDate];
            NSNumber * serverTime = [gisDict objectForKey:@"serverTime"];
            
            NSLog(@"generalItems (gameId %@) %@", game.gameId, [gisDict objectForKey:@"serverTime"]);
            
            [context performBlock:^{
                for (NSDictionary *generalItemDict in [gisDict objectForKey:@"generalItems"]) {
                    [GeneralItem generalItemWithDictionary:generalItemDict
                                                  withGame:game
                                    inManagedObjectContext:context];
                }
                if (serverTime) {
                    [SynchronizationBookKeeping createEntry:@"generalItems"
                                                       time:serverTime
                                                  idContext:game.gameId
                                     inManagedObjectContext:context];
                }
                [run.managedObjectContext save:nil];
                ARLAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
                [appDelegate.arlearnDatabase saveToURL:appDelegate.arlearnDatabase.fileURL forSaveOperation:UIDocumentSaveForCreating completionHandler:^(BOOL succes) {
                    [self synchronizeGeneralItemVisiblityStatementsWithRun:run];
                }];
            }];
        });
    }];
    
}

+ (void) synchronizeGeneralItemsAndVisibilityStatments: (NSNumber *) runId withManagedContext:(NSManagedObjectContext *) context {
    [context performBlock:^{
        Run * run = [Run retrieveRun:runId inManagedObjectContext:context];
        [self synchronizeGeneralItemsAndVisibilityStatments:run];
    }];
}


+ (void) synchronizeGeneralItems: (NSNumber *) gameId withManagedContext:(NSManagedObjectContext *) context {
    [context performBlock:^{
        Game * game = [Game retrieveGame:gameId inManagedObjectContext:context];
        [self synchronizeGeneralItemsWithGame:game];
    }];
}

+ (void) synchronizeGeneralItemVisiblityStatementsWithRun: (Run *) run {
    NSManagedObjectContext * context = run.managedObjectContext;
    //    [context performBlock:^{
    NSNumber * lastDate = [self getLastSynchronizationDate:context type:@"generalItemsVisibility" context:run.runId];
    dispatch_queue_t fetchQ2 = dispatch_queue_create("itemsVisFetcher", NULL);
    dispatch_async(fetchQ2, ^{
        NSDictionary * visDict =[ARLNetwork itemVisibilityForRun:run.runId from:lastDate];
        NSNumber * serverTime = [visDict objectForKey:@"serverTime"];
        
        if ([[visDict objectForKey:@"generalItemsVisibility"] count] > 0) {
            [run.managedObjectContext performBlock:^{
                for (NSDictionary * viStatement in [visDict objectForKey:@"generalItemsVisibility"] ) {
                    [GeneralItemVisibility visibilityWithDictionary: viStatement withRun: run];
                }
            }];
        }
        if (serverTime) {
            [SynchronizationBookKeeping createEntry:@"generalItemsVisibility"
                                               time:serverTime
                                          idContext:run.runId
                             inManagedObjectContext:context];
        }
    });
    //    }];
}

+ (void) synchronizeGeneralItemVisiblityStatements: (NSNumber *) runId withManagedContext:(NSManagedObjectContext *) context {
    [context performBlock:^{
        Run * run = [Run retrieveRun:runId inManagedObjectContext:context];
        [self synchronizeGeneralItemVisiblityStatementsWithRun:run];
    }];
    
}




//private methods

+ (NSNumber*) getLastSynchronizationDate : (NSManagedObjectContext *) context type:(NSString *) type{
    return [self getLastSynchronizationDate:context type:type context:nil];
}

+ (NSNumber*) getLastSynchronizationDate : (NSManagedObjectContext *) managedContext type:(NSString *) type context:(NSNumber *) identifierContext {
    NSString * key = [NSString stringWithFormat:@"%@+%@", type, identifierContext];
    SynchronizationBookKeeping * objectFromCache = [[ARLCloudSynchronizer syncDates] objectForKey:key];
    if (objectFromCache) {
        return objectFromCache.lastSynchronization;
    }
    NSFetchRequest *fetch = [NSFetchRequest fetchRequestWithEntityName:@"SynchronizationBookKeeping"];
    if (identifierContext) {
        fetch.predicate = [NSPredicate predicateWithFormat: @"type = %@ and context = %@", type, identifierContext];
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
        [[ARLCloudSynchronizer syncDates] setObject:bookKeeping forKey:key];
        return bookKeeping.lastSynchronization;
    }
    
}
@end
