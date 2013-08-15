//
//  GeneralItemVisibility.h
//  ARLearn
//
//  Created by Stefaan Ternier on 8/6/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class GeneralItem, Run;

@interface GeneralItemVisibility : NSManagedObject

@property (nonatomic, retain) NSString * email;
@property (nonatomic, retain) NSNumber * generalItemId;
@property (nonatomic, retain) NSNumber * runId;
@property (nonatomic, retain) NSNumber * status;
@property (nonatomic, retain) NSNumber * timeStamp;
@property (nonatomic, retain) Run *correspondingRun;
@property (nonatomic, retain) GeneralItem *generalItem;

@end
