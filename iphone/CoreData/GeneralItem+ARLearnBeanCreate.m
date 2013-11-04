//
//  GeneralItem+ARLearnBeanCreate.m
//  ARLearn
//
//  Created by Stefaan Ternier on 2/3/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "GeneralItem+ARLearnBeanCreate.h"


@implementation GeneralItem (ARLearnBeanCreate)

+ (GeneralItem *) generalItemWithDictionary: (NSDictionary *) giDict
                                 withGameId: (NSNumber * ) gameId
                     inManagedObjectContext: (NSManagedObjectContext * ) context {
    
    Game * game = [Game retrieveGame:gameId inManagedObjectContext:context];
    return [self generalItemWithDictionary:giDict withGame:game inManagedObjectContext:context];
}




+ (GeneralItem *) generalItemWithDictionary: (NSDictionary *) giDict
                                   withGame: (Game * ) game
                     inManagedObjectContext: (NSManagedObjectContext * ) context {
    
    GeneralItem * gi = [self retrieveFromDb:giDict withManagedContext:context];
    if ([[giDict objectForKey:@"deleted"] boolValue]) {
        if (gi) {
            //item is deleted
            [context deleteObject:gi];
        }
        return nil;
    }
    if (!gi) {
        gi = [NSEntityDescription insertNewObjectForEntityForName:@"GeneralItem"
                                           inManagedObjectContext:context];
    }
    
    gi.id = [giDict objectForKey:@"id"] ;
    gi.ownerGame = game;
    gi.gameId = [giDict objectForKey:@"gameId"];
    gi.lat = [giDict objectForKey:@"lat"];
    gi.lng = [giDict objectForKey:@"lng"];
    gi.name = [giDict objectForKey:@"name"];
    gi.richText = [giDict objectForKey:@"richText"];
    gi.sortKey = [giDict objectForKey:@"sortKey"] ;
    gi.type = [giDict objectForKey:@"type"];
    gi.json = [NSKeyedArchiver archivedDataWithRootObject:giDict];
    [self setCorrespondingVisibilityItems:gi];

    [self downloadCorrespondingData:giDict withGeneralItem:gi inManagedObjectContext:context];
    return gi;
    
   }

+ (void) downloadCorrespondingData: (NSDictionary *) giDict
                   withGeneralItem: (GeneralItem *) gi
            inManagedObjectContext: (NSManagedObjectContext * ) context {
    NSDictionary * jsonDict = [NSKeyedUnarchiver unarchiveObjectWithData:gi.json];
    if ([jsonDict objectForKey:@"iconUrl"]) {
        [GeneralItemData createDownloadTask:gi withKey:@"iconUrl" withUrl:[jsonDict objectForKey:@"iconUrl"] withManagedContext:context];
    }
    if ([gi.type caseInsensitiveCompare:@"org.celstec.arlearn2.beans.generalItem.AudioObject"] == NSOrderedSame ){
        [GeneralItemData createDownloadTask:gi withKey:@"audio" withUrl:[jsonDict objectForKey:@"audioFeed"] withManagedContext:context];
    } else if ([gi.type caseInsensitiveCompare:@"org.celstec.arlearn2.beans.generalItem.VideoObject"] == NSOrderedSame ){
        [GeneralItemData createDownloadTask:gi withKey:@"video" withUrl:[jsonDict objectForKey:@"videoFeed"] withManagedContext:context];
    }
//    else {
//        NSLog(@"nothing to download for %@", gi.type);
//    }
    
}

+ (void) setCorrespondingVisibilityItems: (GeneralItem *) gi {
    NSManagedObjectContext * context = gi.managedObjectContext;
    NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"GeneralItemVisibility"];
    request.predicate = [NSPredicate predicateWithFormat:@"generalItemId == %lld", [gi.id longLongValue]];
    
    NSError *error = nil;
    NSArray *allVisibilityStatements = [context executeFetchRequest:request error:&error];
    if (error) {
        NSLog(@"error %@", error);
    }
    for (GeneralItemVisibility *giv in allVisibilityStatements) {
        giv.generalItem = gi;
    }
    
}

+ (GeneralItem *) retrieveFromDbWithId: (NSNumber *) itemId withManagedContext: (NSManagedObjectContext*) context{
    GeneralItem * gi = nil;
    
    NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"GeneralItem"];
    request.predicate = [NSPredicate predicateWithFormat:@"id = %lld", [itemId longLongValue]];
    NSError *error = nil;

    NSArray *generalItemsFromDb = [context executeFetchRequest:request error:&error];
    if (error) {
        NSLog(@"error %@", error);
    }
    if (!generalItemsFromDb || ([generalItemsFromDb count] != 1)) {
        return nil;
    } else {
        gi = [generalItemsFromDb lastObject];
        return gi;
    }
}

+ (GeneralItem *) retrieveFromDb: (NSDictionary *) giDict withManagedContext: (NSManagedObjectContext*) context{
    GeneralItem * gi = nil;
    
    NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"GeneralItem"];
    request.predicate = [NSPredicate predicateWithFormat:@"id = %lld", [[giDict objectForKey:@"id"] longLongValue]];
    NSError *error = nil;

    NSArray *generalItemsFromDb = [context executeFetchRequest:request error:&error];
    if (error) {
        NSLog(@"error %@", error);
    }
    if (!generalItemsFromDb || ([generalItemsFromDb count] != 1)) {
        return nil;
    } else {
        gi = [generalItemsFromDb lastObject];
        return gi;
    }
}

//+ (NSDictionary*) getDatas: (GeneralItem* ) gi withManagedContext: (NSManagedObjectContext*) context{
//    NSMutableArray *objectArray = [NSMutableArray arrayWithArray:[gi.data allObjects]];
//    NSMutableArray *keysArray = [NSMutableArray arrayWithCapacity:[objectArray count]];
//    for (GeneralItemData* data  in objectArray) {
//        [keysArray addObject:data.name];
//    }
//    return  [NSDictionary dictionaryWithObjects:objectArray forKeys:keysArray];
//}

+ (NSArray *) getAll: (NSManagedObjectContext*) context {
    NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"GeneralItem"];
    
    NSError *error = nil;
    NSArray *unsyncedData = [context executeFetchRequest:request error:&error];
    if (error) {
        NSLog(@"error %@", error);
    }
    return unsyncedData;
    
}

+ (NSArray *) retrieve :(NSNumber*) runId withManagedContext: (NSManagedObjectContext*) context{
    NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"GeneralItem"];
    NSNumber * gameId = [Run retrieveRun:runId inManagedObjectContext:context].gameId;
    request.predicate = [NSPredicate predicateWithFormat:@"gameId =%lld ",
                         [gameId longLongValue]
                         ];


    return [context executeFetchRequest:request error:nil];
}

- (NSData *) customIconData {
    for (GeneralItemData* data in self.data) {
                NSLog(@"data %@", data.name);
        if ([data.name isEqualToString:@"iconUrl"]) {
            return data.data;
        }
    }
    return nil;
    
}


@end
