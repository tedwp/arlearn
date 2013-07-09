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
#import "SynchronizationBookKeeping.h"
#import "ARLNetwork.h"
#import "SynchronizationBookKeeping+create.h"
#import "ARLAppDelegate.h"

@interface ARLCloudSynchronizer : NSObject

+ (void) syncronizeRuns: (NSManagedObjectContext *) context;
+ (void) syncronizeGames: (NSManagedObjectContext *) context;
+ (void) synchronizeGeneralItemsWithGame: (Game *) game;
+ (void) synchronizeGeneralItemsAndVisibilityStatments: (Run *) run;
+ (void) synchronizeGeneralItemsAndVisibilityStatments: (NSNumber *) runId withManagedContext:(NSManagedObjectContext *) context ;
+ (void) synchronizeGeneralItems: (NSNumber *) gameId withManagedContext:(NSManagedObjectContext *) context;
+ (void) synchronizeGeneralItemVisiblityStatementsWithRun: (Run *) run;
+ (void) synchronizeGeneralItemVisiblityStatements: (NSNumber *) runId withManagedContext:(NSManagedObjectContext *) context;

@end
