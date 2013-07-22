//
//  GeneralItemVisibility+ARLearnBeanCreate.m
//  ARLearn
//
//  Created by Stefaan Ternier on 2/3/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "GeneralItemVisibility+ARLearnBeanCreate.h"
@implementation GeneralItemVisibility (ARLearnBeanCreate)


+ (GeneralItemVisibility *) visibilityWithDictionary: (NSDictionary *) visDict withRun: (Run * ) run withGeneralItem: (GeneralItem *) generalItem {
    GeneralItemVisibility * giVis = [self retrieveFromDb:visDict withManagedContext:run.managedObjectContext];
    if ([[visDict objectForKey:@"deleted"] boolValue]) {
        //item is deleted
        [giVis.managedObjectContext deleteObject:giVis];
        return nil;
    }
    if (!giVis) {
        giVis = [NSEntityDescription insertNewObjectForEntityForName:@"GeneralItemVisibility"
                                              inManagedObjectContext:run.managedObjectContext];
    }
    giVis.correspondingRun = run;
//    giVis.generalItem = generalItem;
    giVis.timeStamp = [visDict objectForKey:@"title"] ;
    giVis.generalItemId = [visDict objectForKey:@"generalItemId"] ;
    giVis.runId = [visDict objectForKey:@"runId"] ;
    giVis.status = [visDict objectForKey:@"status"] ;
    giVis.timeStamp =[visDict objectForKey:@"timeStamp"] ;
    giVis.email =[visDict objectForKey:@"email"];
    
//    NSLog(@"setting visibility for %@ to %@", generalItem.name, giVis.status);
   
    
    return giVis;
}

+ (GeneralItemVisibility *) visibilityWithDictionary: (NSDictionary *) visDict withRun: (Run * ) run {
    GeneralItem * generalItem = [GeneralItem retrieveFromDbWithId:[visDict objectForKey:@"generalItemId"] withManagedContext:run.managedObjectContext];
    if (generalItem) {
//        NSLog(@"adding a general Item to vis %@", generalItem);
        GeneralItemVisibility * vis = [self visibilityWithDictionary:visDict
                                      withRun:run withGeneralItem:generalItem];
        [generalItem addVisibilityObject:vis];
        return vis;
    }
    NSLog(@"no generalItem found");
    return nil;
    
}



+ (GeneralItemVisibility *) retrieveFromDb: (NSDictionary *) visDict withManagedContext: (NSManagedObjectContext*) context{
    GeneralItemVisibility * giVis = nil;
    NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"GeneralItemVisibility"];
    request.predicate = [NSPredicate predicateWithFormat:@"generalItemId = %lld and email = %@ and runId =%lld and status = %ld",
                         [[visDict objectForKey:@"generalItemId"] longLongValue],
                         [visDict objectForKey:@"email"] ,
                         [[visDict objectForKey:@"runId"] longLongValue],
                         [[visDict objectForKey:@"status"] intValue]
                         ];
    NSError *error = nil;
    
    NSArray *giVises = [context executeFetchRequest:request error:&error];
    if (error) {
        NSLog(@"error %@", error);
    }
    if (!giVises || ([giVises count] != 1)) {
        return nil;
    } else {
        giVis = [giVises lastObject];
        return giVis;
    }
}


+ (void) deleteAll: (NSManagedObjectContext * ) context {
    NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"GeneralItemVisibility"];
    
    NSError *error = nil;
    NSArray *visibilities = [context executeFetchRequest:request error:&error];
    if (error) {
        NSLog(@"error %@", error);
    }
    for (id visi in visibilities) {
        [context deleteObject:visi];
    }
    
}


@end
