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
    NSError * error;
    
    if (![context save:&error]) {
        NSLog(@"error %@", error);
    }
    [self downloadCorrespondingData:giDict withGeneralItem:gi inManagedObjectContext:context];
    return gi;
    
   }

+ (void) downloadCorrespondingData: (NSDictionary *) giDict
                   withGeneralItem: (GeneralItem *) gi
            inManagedObjectContext: (NSManagedObjectContext * ) context {
    if ([gi.type caseInsensitiveCompare:@"org.celstec.arlearn2.beans.generalItem.AudioObject"] == NSOrderedSame ){
        NSLog(@"about to download audio");
        NSString * audioFeed = [giDict objectForKey:@"audioFeed"];
        GeneralItemData * giData = [NSEntityDescription insertNewObjectForEntityForName:@"GeneralItemData" inManagedObjectContext:context];
        giData.name = @"audio";
        NSURL  *url = [NSURL URLWithString:audioFeed];
        NSData *urlData = [NSData dataWithContentsOfURL:url];
        if ( urlData ){
            giData.data = urlData;
            giData.generalItem = gi;
        }
        NSError * error;

        if (![context save:&error]) {
            NSLog(@"error %@", error);
        }
    } else if ([gi.type caseInsensitiveCompare:@"org.celstec.arlearn2.beans.generalItem.VideoObject"] == NSOrderedSame ){
        NSLog(@"about to download video");
        NSString * videoFeed = [giDict objectForKey:@"videoFeed"];
        GeneralItemData * giData = [NSEntityDescription insertNewObjectForEntityForName:@"GeneralItemData" inManagedObjectContext:context];
        giData.name = @"video";
        NSURL  *url = [NSURL URLWithString:videoFeed];
        NSData *urlData = [NSData dataWithContentsOfURL:url];
        if ( urlData ){
            giData.data = urlData;
            giData.generalItem = gi;
        }
        NSError * error;
        
        if (![context save:&error]) {
            NSLog(@"error %@", error);
        }
    } else{
        NSLog(@"not downloading %@", gi.type);
    }
    
}

+ (void) setCorrespondingVisibilityItems: (GeneralItem *) gi {
    NSManagedObjectContext * context = gi.managedObjectContext;
    NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"GeneralItemVisibility"];
    request.predicate = [NSPredicate predicateWithFormat:@"generalItemId == %lld", [gi.id longLongValue]];
    
    NSError *error = nil;
    NSArray *allVisibilityStatements = [context executeFetchRequest:request error:&error];
    for (GeneralItemVisibility *giv in allVisibilityStatements) {
        giv.generalItem = gi;
    }
    
}

+ (GeneralItem *) retrieveFromDbWithId: (NSNumber *) itemId withManagedContext: (NSManagedObjectContext*) context{
    GeneralItem * gi = nil;
    
    NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"GeneralItem"];
    request.predicate = [NSPredicate predicateWithFormat:@"id = %ld", [itemId longValue]];
    NSArray *generalItemsFromDb = [context executeFetchRequest:request error:nil];
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
    NSArray *generalItemsFromDb = [context executeFetchRequest:request error:nil];
    if (!generalItemsFromDb || ([generalItemsFromDb count] != 1)) {
        return nil;
    } else {
        gi = [generalItemsFromDb lastObject];
        return gi;
    }
    
}
@end
