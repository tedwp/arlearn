//
//  Run.h
//  ARLearn
//
//  Created by Stefaan Ternier on 7/24/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class Action, Game, GeneralItemVisibility, Response;

@interface Run : NSManagedObject

@property (nonatomic, retain) NSNumber * deleted;
@property (nonatomic, retain) NSNumber * gameId;
@property (nonatomic, retain) NSString * owner;
@property (nonatomic, retain) NSNumber * runId;
@property (nonatomic, retain) NSString * title;
@property (nonatomic, retain) NSSet *actions;
@property (nonatomic, retain) Game *game;
@property (nonatomic, retain) NSSet *itemVisibilityRules;
@property (nonatomic, retain) NSSet *responses;
@end

@interface Run (CoreDataGeneratedAccessors)

- (void)addActionsObject:(Action *)value;
- (void)removeActionsObject:(Action *)value;
- (void)addActions:(NSSet *)values;
- (void)removeActions:(NSSet *)values;

- (void)addItemVisibilityRulesObject:(GeneralItemVisibility *)value;
- (void)removeItemVisibilityRulesObject:(GeneralItemVisibility *)value;
- (void)addItemVisibilityRules:(NSSet *)values;
- (void)removeItemVisibilityRules:(NSSet *)values;

- (void)addResponsesObject:(Response *)value;
- (void)removeResponsesObject:(Response *)value;
- (void)addResponses:(NSSet *)values;
- (void)removeResponses:(NSSet *)values;

@end
