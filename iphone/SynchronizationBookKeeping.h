//
//  SynchronizationBookKeeping.h
//  ARLearn
//
//  Created by Stefaan Ternier on 7/24/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>


@interface SynchronizationBookKeeping : NSManagedObject

@property (nonatomic, retain) NSNumber * context;
@property (nonatomic, retain) NSNumber * lastSynchronization;
@property (nonatomic, retain) NSString * type;

@end
