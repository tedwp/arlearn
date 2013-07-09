//
//  SynchronizationBookKeeping.h
//  ARLearn
//
//  Created by Stefaan Ternier on 2/6/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>


@interface SynchronizationBookKeeping : NSManagedObject

@property (nonatomic, retain) NSNumber * lastSynchronization;
@property (nonatomic, retain) NSString * type;
@property (nonatomic, retain) NSNumber * context;

@end
