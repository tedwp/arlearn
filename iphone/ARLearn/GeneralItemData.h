//
//  GeneralItemData.h
//  ARLearn
//
//  Created by Stefaan Ternier on 7/5/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class GeneralItem;

@interface GeneralItemData : NSManagedObject

@property (nonatomic, retain) NSData * data;
@property (nonatomic, retain) NSString * name;
@property (nonatomic, retain) GeneralItem *generalItem;

@end
