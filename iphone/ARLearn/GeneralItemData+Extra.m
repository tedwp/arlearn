//
//  GeneralItemData+Extra.m
//  ARLearn
//
//  Created by Stefaan Ternier on 7/16/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "GeneralItemData+Extra.h"

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
@end
