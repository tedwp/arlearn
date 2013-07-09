//
//  Run+ARLearnBeanCreate.h
//  ARLearn
//
//  Created by Stefaan Ternier on 2/2/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "Run.h"

@interface Run (ARLearnBeanCreate)

+ (Run *) retrieveRun: (NSNumber *) runId inManagedObjectContext: (NSManagedObjectContext * ) context;
+ (Run *) runWithDictionary: (NSDictionary *) runDict inManagedObjectContext: (NSManagedObjectContext * ) context;
+ (void) deleteAllRuns: (NSManagedObjectContext * ) context;
@end
