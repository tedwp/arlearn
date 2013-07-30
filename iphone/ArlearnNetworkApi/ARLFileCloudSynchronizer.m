//
//  ARLFileCloudSynchronizer.m
//  ARLearn
//
//  Created by Stefaan Ternier on 7/16/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLFileCloudSynchronizer.h"

@implementation ARLFileCloudSynchronizer

@synthesize context = _context;


- (void) createContext: (NSManagedObjectContext*) mainContext {
    self.parentContext = mainContext;
    self.context = [[NSManagedObjectContext alloc] initWithConcurrencyType:NSPrivateQueueConcurrencyType];
    self.context.parentContext = mainContext;
}

- (void) sync {
    [self.context performBlock:^{
        [self asyncExecution];
    }];
    
}


- (void) asyncExecution {
    [self downloadGeneralItems];
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
            NSLog(@"save completed");
            [self.parentContext performBlock:^{
                NSError *error = nil;
                if (![self.parentContext save:&error]) {abort();}
            }];
            
        }
        NSLog(@"save completed");
        
    }
    NSLog(@"save completed");
}

- (void) downloadGeneralItems {
    for (GeneralItemData* giData in [GeneralItemData getUnsyncedData:self.context]) {
        NSLog(@"gidata url = %@ replicated = %@ error = %@ ", giData.url, giData.replicated, giData.error);
        NSURL  *url = [NSURL URLWithString:giData.url];
        NSData *urlData = [NSData dataWithContentsOfURL:url];
        if ( urlData ){
            giData.data = urlData;
            giData.replicated = [NSNumber numberWithBool:YES];
        } else {
            NSLog(@"sth went wrong");
        }
        
        [self saveContext];
    }
}


@end
