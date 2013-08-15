//
//  Action.h
//  ARLearn
//
//  Created by Stefaan Ternier on 8/6/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class Account, GeneralItem, Run;

@interface Action : NSManagedObject

@property (nonatomic, retain) NSString * action;
@property (nonatomic, retain) NSNumber * synchronized;
@property (nonatomic, retain) NSNumber * time;
@property (nonatomic, retain) Account *account;
@property (nonatomic, retain) GeneralItem *generalItem;
@property (nonatomic, retain) Run *run;

@end
