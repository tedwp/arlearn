//
//  ARLCloudSynchronizer.m
//  ARLearn
//
//  Created by Stefaan Ternier on 2/4/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLCloudSynchronizer.h"

@implementation ARLCloudSynchronizer

@synthesize syncRuns = _syncRuns;
@synthesize syncGames = _syncGames;
@synthesize syncResponses = _syncResponses;
@synthesize gameId = _gameId;
@synthesize visibilityRunId = _visibilityRunId;
@synthesize context = _context;

static NSMutableDictionary *syncDates;

+ (NSMutableDictionary *) syncDates {
    if (syncDates == nil) {
        syncDates = [[NSMutableDictionary alloc] init];
    }
    return syncDates;
}

+ (void) syncResponses: (NSManagedObjectContext*) context {
    ARLCloudSynchronizer* synchronizer = [[ARLCloudSynchronizer alloc] init];
    //    ARLAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
    [synchronizer createContext:context];
    synchronizer.syncResponses = YES;
    [synchronizer sync];
}

- (void) sync {
    [self.context performBlock:^{
        [self asyncExecution];
    }];
    
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
            [self.parentContext performBlock:^{
                NSError *error = nil;
                if (![self.parentContext save:&error]) {abort();}
                ARLFileCloudSynchronizer* fileSync = [[ARLFileCloudSynchronizer alloc] init];
                [fileSync createContext:self.parentContext];
                [fileSync sync];
            }];
        }
    }
}

- (void) createContext: (NSManagedObjectContext*) mainContext {
    self.parentContext = mainContext;
    self.context = [[NSManagedObjectContext alloc] initWithConcurrencyType:NSPrivateQueueConcurrencyType];
    self.context.parentContext = mainContext;
}

//- (void)_mocDidSaveNotificationCloud:(NSNotification *)notification
//{
//    NSManagedObjectContext *savedContext = [notification object];
//    if (self.context == savedContext) {
//        return;
//    }
//    if (self.context.persistentStoreCoordinator != savedContext.persistentStoreCoordinator){
//        return;
//    }
//        [self.context mergeChangesFromContextDidSaveNotification:notification];
////    dispatch_sync(queue, ^{
////         NSLog(@"ARLCloud before merging");
////
////        NSLog(@"ARLCloud ready with merging");
////    });
//}

- (void) asyncExecution {
    if (self.syncRuns) {
        [self syncronizeRuns];
        [self asyncExecution];
    } else if (self.syncGames) {
        [self syncronizeGames];
        [self asyncExecution];
    } else if (self.gameId) {
        [self synchronizeGeneralItemsWithGame];
        [self asyncExecution];
    } else if (self.visibilityRunId) {
        [self synchronizeGeneralItemsAndVisibilityStatements];
        [self asyncExecution];
    } else if (self.syncResponses){
        [self synchronizeResponses];
        [self asyncExecution];
    } else {
        [self saveContext];
        //        [self.context reset];
        //        [[NSNotificationCenter defaultCenter] removeObserver:self];
        //        self.context = nil;
        //        [self.context = nil];
    }
    
}




- (void) syncronizeRuns{ //: (NSManagedObjectContext *) context
    
    //    [context performBlock:^{
    NSNumber * lastDate = [ARLCloudSynchronizer getLastSynchronizationDate:self.context type:@"myRuns"];
    //    dispatch_queue_t fetchQ = dispatch_queue_create("Run fetcher", NULL);
    //    dispatch_async(fetchQ, ^{
    NSDictionary * dict = [ARLNetwork runsParticipateFrom:lastDate];
    NSNumber * serverTime = [dict objectForKey:@"serverTime"];
    NSLog(@"allruns %@", [dict objectForKey:@"serverTime"]);
    //            [context performBlock:^{
    for (NSDictionary *run in [dict objectForKey:@"runs"]) {
        [Run runWithDictionary:run inManagedObjectContext:self.context];
    }
    
    if (serverTime) {
        [SynchronizationBookKeeping createEntry:@"myRuns" time:serverTime inManagedObjectContext:self.context];
    }
    
    //    [self saveContext];
    self.syncRuns = NO;
}

- (void) syncronizeGames { //: (NSManagedObjectContext *) context{
    NSNumber * lastDate = [ARLCloudSynchronizer getLastSynchronizationDate:self.context type:@"myGames"];
    NSDictionary * gdict = [ARLNetwork gamesParticipateFrom:lastDate];
    NSNumber * serverTime = [gdict objectForKey:@"serverTime"];
    
    for (NSDictionary *game in [gdict objectForKey:@"games"]) {
        [Game gameWithDictionary:game inManagedObjectContext:self.context];
    }
    if (serverTime) {
        [SynchronizationBookKeeping createEntry:@"myGames" time:serverTime inManagedObjectContext:self.context];
    }
    //    [self saveContext];
    
    self.syncGames = NO;
}

- (void) synchronizeGeneralItemsWithGame {//: (Game *) game {
    NSNumber * lastDate = [ARLCloudSynchronizer getLastSynchronizationDate:self.context type:@"generalItems" context:self.gameId];
    NSDictionary * gisDict = [ARLNetwork itemsForGameFrom:self.gameId from:lastDate];
    NSNumber * serverTime = [gisDict objectForKey:@"serverTime"];
    Game * game = [Game retrieveGame:self.gameId inManagedObjectContext:self.context];
    
    
    for (NSDictionary *generalItemDict in [gisDict objectForKey:@"generalItems"]) {
        [GeneralItem generalItemWithDictionary:generalItemDict
                                      withGame:game
                        inManagedObjectContext:self.context];
    }
    if (serverTime) {
        [SynchronizationBookKeeping createEntry:@"generalItems"
                                           time:serverTime
                                      idContext:self.gameId
                         inManagedObjectContext:self.context];
    }
    //    [self saveContext];
    
    self.gameId = nil;
    
    
}


- (void) synchronizeGeneralItemsAndVisibilityStatements {
    Run * run = [Run retrieveRun:self.visibilityRunId inManagedObjectContext:self.context];
    [self synchronizeGeneralItemsAndVisibilityStatements:run];
    self.visibilityRunId = nil;
}

- (void) synchronizeGeneralItemsAndVisibilityStatements: (Run *) run {
    NSNumber * lastDate = [ARLCloudSynchronizer getLastSynchronizationDate:self.context type:@"generalItemsVisibility" context:run.runId];
    NSDictionary * visDict =[ARLNetwork itemVisibilityForRun:run.runId from:lastDate];
    NSNumber * serverTime = [visDict objectForKey:@"serverTime"];
    
    if ([[visDict objectForKey:@"generalItemsVisibility"] count] > 0) {
        for (NSDictionary * viStatement in [visDict objectForKey:@"generalItemsVisibility"] ) {
            [GeneralItemVisibility visibilityWithDictionary: viStatement withRun: run];
        }
    }
    if (serverTime) {
        [SynchronizationBookKeeping createEntry:@"generalItemsVisibility"
                                           time:serverTime
                                      idContext:run.runId
                         inManagedObjectContext:self.context];
        
    }
}

- (void) synchronizeResponses {
    NSArray* responses =  [Response getUnsyncedReponses:self.context];
    for (Response* resp in responses) {
        NSLog(@"resp %@ sync %@ runId %@", resp.value, resp.synchronized, resp.run.runId);
        if (resp.value) {
            [ARLNetwork publishResponse:resp.run.runId responseValue:resp.value itemId:resp.generalItem.id timeStamp:resp.timeStamp];
            resp.synchronized = [NSNumber numberWithBool:YES];
        } else {
            u_int32_t random = arc4random();
            NSString* imageName = [NSString stringWithFormat:@"%d.jpg", random];
            NSLog(@"runId %@", resp.run.runId);
            if (resp.run.runId) {
                NSString* uploadUrl = [ARLNetwork requestUploadUrl:imageName withRun:resp.run.runId];
                NSLog(@"time to upload some data :) to %@", uploadUrl);
                [ARLNetwork perfomUpload: uploadUrl withFileName:imageName contentType:@"application/jpg" withData:resp.data];

                NSString * serverUrl = [NSString stringWithFormat:@"%@/uploadService/%@/%@:%@/%@", serviceUrl, resp.run.runId,
                      [[NSUserDefaults standardUserDefaults] objectForKey:@"accountType"],
                      [[NSUserDefaults standardUserDefaults] objectForKey:@"accountLocalId"],imageName];
                NSLog(@"server Url: %@", serverUrl);
                
                NSDictionary *myDictionary= [[NSDictionary alloc] initWithObjectsAndKeys:
                                             resp.width, @"width",
                                             resp.height, @"height",
                                             serverUrl, @"imageUrl", nil];
                NSString* jsonString = [NSString jsonString:myDictionary];
                
                
                [ARLNetwork publishResponse:resp.run.runId responseValue:jsonString itemId:resp.generalItem.id timeStamp:resp.timeStamp];
                
                //TODO publish the response
                
                resp.synchronized = [NSNumber numberWithBool:YES];
            }
            
        }
        
    }
    self.syncResponses = NO;
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
