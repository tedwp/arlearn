//
//  GeneralItem+ARLearnBeanCreate.h
//  ARLearn
//
//  Created by Stefaan Ternier on 2/3/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "GeneralItem.h"
#import "GeneralItemVisibility.h"
#import "Game+ARLearnBeanCreate.h"
#import "GeneralItemData.h"
#import "GeneralItemData+Extra.h"
#import "Run+ARLearnBeanCreate.h"

@interface GeneralItem (ARLearnBeanCreate)

+ (GeneralItem *) generalItemWithDictionary: (NSDictionary *) giDict withGameId: (NSNumber * ) gameId inManagedObjectContext: (NSManagedObjectContext * ) context;
+ (GeneralItem *) generalItemWithDictionary: (NSDictionary *) giDict withGame: (Game * ) game inManagedObjectContext: (NSManagedObjectContext * ) context;
+ (GeneralItem *) retrieveFromDbWithId: (NSNumber *) itemId withManagedContext: (NSManagedObjectContext*) context;

+ (NSArray *) getAll: (NSManagedObjectContext*) context;
+ (NSArray *) retrieve :(NSNumber*) runId withManagedContext: (NSManagedObjectContext*) context;
-  (NSData *) customIconData;
@end
