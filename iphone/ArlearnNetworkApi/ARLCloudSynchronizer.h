//
//  ARLCloudSynchronizer.h
//  ARLearn
//
//  Created by Stefaan Ternier on 2/4/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Run+ARLearnBeanCreate.h"
#import "Game+ARLearnBeanCreate.h"
#import "GeneralItem+ARLearnBeanCreate.h"
#import "GeneralItemVisibility+ARLearnBeanCreate.h"
#import "Response+Create.h"
#import "Action+Create.h"
#import "SynchronizationBookKeeping.h"
#import "ARLNetwork.h"
#import "SynchronizationBookKeeping+create.h"
#import "ARLAppDelegate.h"
#import "ARLFileCloudSynchronizer.h"

@interface ARLCloudSynchronizer : NSObject {
    BOOL syncRuns;
    BOOL syncGames;
}


@property (nonatomic, readwrite) BOOL syncRuns;
@property (nonatomic, readwrite) BOOL syncGames;
@property (nonatomic, readwrite) BOOL syncResponses;
@property (nonatomic, readwrite) BOOL syncActions;
@property (strong, nonatomic)  NSNumber * gameId;

@property (strong, nonatomic)  NSNumber * visibilityRunId;

@property (strong, nonatomic)  NSManagedObjectContext * context;
@property (strong, nonatomic)  NSManagedObjectContext * parentContext;

+ (void) syncGamesAndRuns: (NSManagedObjectContext*) context;
+ (void) syncResponses:  (NSManagedObjectContext*) context;
+ (void) syncActions: (NSManagedObjectContext*) context ;

- (void) sync;
- (void) createContext: (NSManagedObjectContext*) mainContext;

//+ (void) synchronizeGeneralItemsWithGame: (Game *) game;
//+ (void) synchronizeGeneralItems: (NSNumber *) gameId withManagedContext:(NSManagedObjectContext *) context;
//+ (void) synchronizeGeneralItemVisiblityStatementsWithRun: (Run *) run;
//+ (void) synchronizeGeneralItemVisiblityStatements: (NSNumber *) runId withManagedContext:(NSManagedObjectContext *) context;

@end
