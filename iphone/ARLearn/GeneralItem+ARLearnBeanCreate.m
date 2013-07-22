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
        //item is deleted
        [gi.managedObjectContext deleteObject:gi];
        [gi.managedObjectContext save:nil];
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

//        NSDictionary* dataMap = [self getDatas:gi withManagedContext:context];
//        GeneralItemData * giData = [dataMap objectForKey:@"audio"];
//        if (!giData) {
//            giData = [NSEntityDescription insertNewObjectForEntityForName:@"GeneralItemData" inManagedObjectContext:context];
//            giData.name = @"audio";
//            giData.generalItem = gi;
//        }
//        if (![[giDict objectForKey:@"audioFeed"] isEqual:giData.url]) {
//            giData.url = [giDict objectForKey:@"audioFeed"];
//            giData.replicated = [NSNumber numberWithBool:NO];
//            giData.error = [NSNumber numberWithBool:NO];
//            
//        } else {
//            if (giData.data == nil && [giData.replicated isEqualToNumber:[NSNumber numberWithBool:NO]]) {
//                 giData.error = [NSNumber numberWithBool:NO];
//            }
//        }
    } else{
        NSLog(@"nothing to download for %@", gi.type);
    }
    
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
    request.predicate = [NSPredicate predicateWithFormat:@"id = %ld", [itemId longValue]];
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
    request.predicate = [NSPredicate predicateWithFormat:@"id = %ld", [[giDict objectForKey:@"id"] longValue]];
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

- (NSData *) icon {
    for (GeneralItemData* data in self.data) {
                NSLog(@"data %@", data.name);
        if ([data.name isEqualToString:@"iconUrl"]) {
            return data.data;
        }
    }
    return nil;
    
}


@end
