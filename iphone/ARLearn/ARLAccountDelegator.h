//
//  ARLAccountDelegator.h
//  ARLearn
//
//  Created by Stefaan Ternier on 7/8/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "Account+Create.h"
@interface ARLAccountDelegator : NSObject

+ (void) deleteCurrentAccount: (NSManagedObjectContext * ) context ;

+ (void) resetAccount:  (NSManagedObjectContext * ) context;

@end
