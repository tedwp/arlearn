//
//  Account+Create.h
//  ARLearn
//
//  Created by Stefaan Ternier on 8/6/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "Account.h"

@interface Account (Create)
+ (Account *) accountWithDictionary: (NSDictionary *) acDict inManagedObjectContext: (NSManagedObjectContext * ) context;
+ (Account *) retrieveFromDb: (NSDictionary *) giDict withManagedContext: (NSManagedObjectContext*) context;
+ (Account *) retrieveFromDbWithLocalId: (NSString *) localId withManagedContext: (NSManagedObjectContext*) context;
+ (void) deleteAll: (NSManagedObjectContext * ) context ;
@end
