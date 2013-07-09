//
//  Account+create.h
//  ARLearn
//
//  Created by Stefaan Ternier on 7/8/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "Account.h"

@interface Account (create)

+ (Account *) accountWithDictionary: (NSDictionary *) acDict inManagedObjectContext: (NSManagedObjectContext * ) context;
+ (Account *) retrieveFromDb: (NSDictionary *) giDict withManagedContext: (NSManagedObjectContext*) context;
@end
