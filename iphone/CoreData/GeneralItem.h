//
//  GeneralItem.h
//  ARLearn
//
//  Created by Stefaan Ternier on 8/6/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class Action, CurrentItemVisibility, Game, GeneralItemData, GeneralItemVisibility, Response;

@interface GeneralItem : NSManagedObject

@property (nonatomic, retain) NSString * descriptionText;
@property (nonatomic, retain) NSNumber * gameId;
@property (nonatomic, retain) NSNumber * id;
@property (nonatomic, retain) NSData * json;
@property (nonatomic, retain) NSNumber * lat;
@property (nonatomic, retain) NSNumber * lng;
@property (nonatomic, retain) NSString * name;
@property (nonatomic, retain) NSString * richText;
@property (nonatomic, retain) NSNumber * sortKey;
@property (nonatomic, retain) NSString * type;
@property (nonatomic, retain) NSSet *actions;
@property (nonatomic, retain) NSSet *currentVisibility;
@property (nonatomic, retain) NSSet *data;
@property (nonatomic, retain) Game *ownerGame;
@property (nonatomic, retain) NSSet *responses;
@property (nonatomic, retain) NSSet *visibility;
@end

@interface GeneralItem (CoreDataGeneratedAccessors)

- (void)addActionsObject:(Action *)value;
- (void)removeActionsObject:(Action *)value;
- (void)addActions:(NSSet *)values;
- (void)removeActions:(NSSet *)values;

- (void)addCurrentVisibilityObject:(CurrentItemVisibility *)value;
- (void)removeCurrentVisibilityObject:(CurrentItemVisibility *)value;
- (void)addCurrentVisibility:(NSSet *)values;
- (void)removeCurrentVisibility:(NSSet *)values;

- (void)addDataObject:(GeneralItemData *)value;
- (void)removeDataObject:(GeneralItemData *)value;
- (void)addData:(NSSet *)values;
- (void)removeData:(NSSet *)values;

- (void)addResponsesObject:(Response *)value;
- (void)removeResponsesObject:(Response *)value;
- (void)addResponses:(NSSet *)values;
- (void)removeResponses:(NSSet *)values;

- (void)addVisibilityObject:(GeneralItemVisibility *)value;
- (void)removeVisibilityObject:(GeneralItemVisibility *)value;
- (void)addVisibility:(NSSet *)values;
- (void)removeVisibility:(NSSet *)values;

@end
