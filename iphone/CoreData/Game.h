//
//  Game.h
//  ARLearn
//
//  Created by Stefaan Ternier on 8/13/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class GeneralItem, Run;

@interface Game : NSManagedObject

@property (nonatomic, retain) NSString * creator;
@property (nonatomic, retain) NSNumber * gameId;
@property (nonatomic, retain) NSNumber * hasMap;
@property (nonatomic, retain) NSString * owner;
@property (nonatomic, retain) NSString * title;
@property (nonatomic, retain) NSString * richTextDescription;
@property (nonatomic, retain) NSSet *correspondingRuns;
@property (nonatomic, retain) NSSet *hasItems;
@end

@interface Game (CoreDataGeneratedAccessors)

- (void)addCorrespondingRunsObject:(Run *)value;
- (void)removeCorrespondingRunsObject:(Run *)value;
- (void)addCorrespondingRuns:(NSSet *)values;
- (void)removeCorrespondingRuns:(NSSet *)values;

- (void)addHasItemsObject:(GeneralItem *)value;
- (void)removeHasItemsObject:(GeneralItem *)value;
- (void)addHasItems:(NSSet *)values;
- (void)removeHasItems:(NSSet *)values;

@end
