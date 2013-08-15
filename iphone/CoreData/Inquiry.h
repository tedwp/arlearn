//
//  Inquiry.h
//  ARLearn
//
//  Created by Stefaan Ternier on 8/8/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class Run;

@interface Inquiry : NSManagedObject

@property (nonatomic, retain) NSNumber * inquiryId;
@property (nonatomic, retain) NSString * title;
@property (nonatomic, retain) NSString * url;
@property (nonatomic, retain) NSData * icon;
@property (nonatomic, retain) NSString * desc;
@property (nonatomic, retain) Run *run;

@end
