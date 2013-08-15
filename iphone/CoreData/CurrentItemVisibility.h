//
//  CurrentItemVisibility.h
//  ARLearn
//
//  Created by Stefaan Ternier on 8/6/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class GeneralItem, Run;

@interface CurrentItemVisibility : NSManagedObject

@property (nonatomic, retain) NSNumber * visible;
@property (nonatomic, retain) GeneralItem *item;
@property (nonatomic, retain) Run *run;

@end
