//
//  GeneralItemData+Extra.m
//  ARLearn
//
//  Created by Stefaan Ternier on 7/16/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "GeneralItemData+Extra.h"
#import "GeneralItem+ARLearnBeanCreate.h"

@implementation GeneralItemData (Extra)


+ (NSArray *) getUnsyncedData: (NSManagedObjectContext*) context {
    NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"GeneralItemData"];

    request.predicate = [NSPredicate predicateWithFormat:@"replicated = %d and error = %d", NO, NO];
    
    NSError *error = nil;
    NSArray *unsyncedData = [context executeFetchRequest:request error:&error];
    if (error) {
        NSLog(@"error %@", error);
    }
    return unsyncedData;
}

+ (void) createDownloadTask: (GeneralItem* ) gi
                    withKey: (NSString*) key
                    withUrl: (NSString*) url
         withManagedContext: (NSManagedObjectContext*) context{
    NSDictionary* dataMap = [self getDatas:gi withManagedContext:context];
    GeneralItemData * giData = [dataMap objectForKey:key];
    if (!giData) {
        giData = [NSEntityDescription insertNewObjectForEntityForName:@"GeneralItemData" inManagedObjectContext:context];
        giData.name = key;
        giData.generalItem = gi;
    }
    if (![url isEqual:giData.url]) {
        giData.url = url;
        giData.replicated = [NSNumber numberWithBool:NO];
        giData.error = [NSNumber numberWithBool:NO];
        
    } else {
        if (giData.data == nil && [giData.replicated isEqualToNumber:[NSNumber numberWithBool:NO]]) {
            giData.error = [NSNumber numberWithBool:NO];
        }
    }
}

+ (NSDictionary*) getDatas: (GeneralItem* ) gi withManagedContext: (NSManagedObjectContext*) context{
    NSMutableArray *objectArray = [NSMutableArray arrayWithArray:[gi.data allObjects]];
    NSMutableArray *keysArray = [NSMutableArray arrayWithCapacity:[objectArray count]];
    for (GeneralItemData* data  in objectArray) {
        [keysArray addObject:data.name];
    }
    return  [NSDictionary dictionaryWithObjects:objectArray forKeys:keysArray];
}

@end
