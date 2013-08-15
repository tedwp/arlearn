//
//  CurrentItemVisibility+Create.h
//  ARLearn
//
//  Created by Stefaan Ternier on 8/6/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "CurrentItemVisibility.h"
#import "Run.h"
#import "GeneralItem+ARLearnBeanCreate.h"
#import "GeneralItemVisibility+ARLearnBeanCreate.h"
#import "ARLAppearDisappearDelegator.h"

@interface CurrentItemVisibility (Create)
+ (CurrentItemVisibility *) create: (GeneralItem *) generalItem withRun: (Run * ) run  ;
+ (void) updateVisibility : (NSNumber*) runId withManagedContext: (NSManagedObjectContext*) context;
+ (void) updateVisibility : (NSNumber *) itemId runId:(NSNumber*) runId withManagedContext: (NSManagedObjectContext*) context;
+ (CurrentItemVisibility *) retrieve: (NSNumber *) itemId runId:(NSNumber*) runId withManagedContext: (NSManagedObjectContext*) context;
+ (NSArray *) retrieveVisibleFor: (NSNumber*) runId withManagedContext: (NSManagedObjectContext*) context;
@end
