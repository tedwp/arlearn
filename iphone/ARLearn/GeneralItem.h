//
//  GeneralItem.h
//  ARLearn
//
//  Created by Stefaan Ternier on 7/5/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class Game, GeneralItemData, GeneralItemVisibility;

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
@property (nonatomic, retain) Game *ownerGame;
@property (nonatomic, retain) NSSet *visibility;
@property (nonatomic, retain) NSSet *data;
@end

@interface GeneralItem (CoreDataGeneratedAccessors)

- (void)addVisibilityObject:(GeneralItemVisibility *)value;
- (void)removeVisibilityObject:(GeneralItemVisibility *)value;
- (void)addVisibility:(NSSet *)values;
- (void)removeVisibility:(NSSet *)values;

- (void)addDataObject:(GeneralItemData *)value;
- (void)removeDataObject:(GeneralItemData *)value;
- (void)addData:(NSSet *)values;
- (void)removeData:(NSSet *)values;



@end
