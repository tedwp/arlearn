//
//  ARLFileCloudSynchronizer.h
//  ARLearn
//
//  Created by Stefaan Ternier on 7/16/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ARLNetwork.h"
#import "ARLAppDelegate.h"
#import "GeneralItemData+Extra.h"

@interface ARLFileCloudSynchronizer : NSObject {
    dispatch_queue_t queue;

}

@property (strong, nonatomic)  NSManagedObjectContext * context;
@property (strong, nonatomic)  NSManagedObjectContext * parentContext;

- (void) createContext: (NSManagedObjectContext*) mainContext;

- (void) sync;

@end
