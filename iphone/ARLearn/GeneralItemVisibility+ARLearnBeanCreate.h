//
//  GeneralItemVisibility+ARLearnBeanCreate.h
//  ARLearn
//
//  Created by Stefaan Ternier on 2/3/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "GeneralItemVisibility.h"

#import "Run.h"
#import "Game.h"
#import "GeneralItem+ARLearnBeanCreate.h"

@interface GeneralItemVisibility (ARLearnBeanCreate)
+ (GeneralItemVisibility *) visibilityWithDictionary: (NSDictionary *) visDict withRun: (Run * ) run withGeneralItem: (GeneralItem *) gi;
+ (GeneralItemVisibility *) visibilityWithDictionary: (NSDictionary *) visDict withRun: (Run * ) run ;
+ (void) deleteAll: (NSManagedObjectContext * ) context;

@end
