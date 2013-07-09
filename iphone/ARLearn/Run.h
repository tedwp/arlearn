//
//  Run.h
//  ARLearn
//
//  Created by Stefaan Ternier on 2/6/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class Game, GeneralItemVisibility;

@interface Run : NSManagedObject

@property (nonatomic, retain) NSNumber * deleted;
@property (nonatomic, retain) NSNumber * gameId;
@property (nonatomic, retain) NSString * owner;
@property (nonatomic, retain) NSNumber * runId;
@property (nonatomic, retain) NSString * title;
@property (nonatomic, retain) Game *game;
@property (nonatomic, retain) GeneralItemVisibility *itemVisibilityRules;

@end
