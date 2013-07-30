//
//  Response.h
//  ARLearn
//
//  Created by Stefaan Ternier on 7/24/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class Account, GeneralItem, Run;

@interface Response : NSManagedObject

@property (nonatomic, retain) NSData * data;
@property (nonatomic, retain) NSNumber * height;
@property (nonatomic, retain) NSNumber * synchronized;
@property (nonatomic, retain) NSNumber * timeStamp;
@property (nonatomic, retain) NSString * value;
@property (nonatomic, retain) NSNumber * width;
@property (nonatomic, retain) Account *account;
@property (nonatomic, retain) GeneralItem *generalItem;
@property (nonatomic, retain) Run *run;

@end
