//
//  Account.h
//  ARLearn
//
//  Created by Stefaan Ternier on 7/8/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>


@interface Account : NSManagedObject

@property (nonatomic, retain) NSString * localId;
@property (nonatomic, retain) NSNumber * accountType;
@property (nonatomic, retain) NSString * email;
@property (nonatomic, retain) NSString * name;
@property (nonatomic, retain) NSString * givenName;
@property (nonatomic, retain) NSString * familyName;
@property (nonatomic, retain) NSData * picture;
@property (nonatomic, retain) NSNumber * accountLevel;

@end
