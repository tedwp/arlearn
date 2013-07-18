//
//  Response+Create.m
//  ARLearn
//
//  Created by Stefaan Ternier on 7/15/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "Response+Create.h"

@implementation Response (Create)


+ (Response *) initResponse: (Run *) run
             forGeneralItem:(GeneralItem *) gi
                  withValue:(NSString *) value
     inManagedObjectContext: (NSManagedObjectContext * ) context {
    Response * response = [NSEntityDescription insertNewObjectForEntityForName:@"Response" inManagedObjectContext: context];
    response.value = value;
    response.generalItem = gi;
    NSLog(@"setting runid to %@", run.runId);

    response.run = run;
    response.synchronized = [NSNumber numberWithBool:NO];
    response.timeStamp = [NSNumber numberWithDouble:[[NSDate date] timeIntervalSince1970]*1000];

    return response;
}

+ (Response *) initResponse: (Run *) run
             forGeneralItem:(GeneralItem *) gi
                  withData:(NSData *) data
     inManagedObjectContext: (NSManagedObjectContext * ) context {
    Response * response = [NSEntityDescription insertNewObjectForEntityForName:@"Response" inManagedObjectContext: context];
    response.value = nil;
    response.data = data;
    response.generalItem = gi;
    response.run = run;
    response.synchronized = [NSNumber numberWithBool:NO];
    response.timeStamp = [NSNumber numberWithDouble:[[NSDate date] timeIntervalSince1970]*1000];
    
    return response;
}

+ (void) deleteAll: (NSManagedObjectContext * ) context {
    NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"Response"];
    
    NSError *error = nil;
    NSArray *responses = [context executeFetchRequest:request error:&error];
    if (error) {
        NSLog(@"error %@", error);
    }
    for (id response in responses) {
        [context deleteObject:response];
    }
    
}

+ (NSArray *) getUnsyncedReponses: (NSManagedObjectContext*) context {
    NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"Response"];
    
    request.predicate = [NSPredicate predicateWithFormat:@"synchronized = %d", NO];
    
    NSError *error = nil;
    NSArray *unsyncedResponses = [context executeFetchRequest:request error:&error];
    if (error) {
        NSLog(@"error %@", error);
    }
    return unsyncedResponses;
}

+ (void) createTextResponse: (NSString *) text
                    withRun: (Run*)run
            withGeneralItem: (GeneralItem*) generalItem {
    NSDictionary *myDictionary= [[NSDictionary alloc] initWithObjectsAndKeys:
                                 text, @"text", nil];
    NSString* jsonString = [NSString jsonString:myDictionary];
    [Response initResponse:run forGeneralItem:generalItem
                 withValue:[NSString jsonString:myDictionary]
    inManagedObjectContext: generalItem.managedObjectContext];
}

+ (void) createImageResponse:(NSData *) data
                       width: (NSNumber*) width
                       height: (NSNumber*) height
                     withRun: (Run*)run
             withGeneralItem: (GeneralItem*) generalItem {
//    NSDictionary *myDictionary= [[NSDictionary alloc] initWithObjectsAndKeys:
//                                 text, @"text", nil];
//    NSString* jsonString = [NSString jsonString:myDictionary];
    
   Response * response = [Response initResponse:run
            forGeneralItem:generalItem
                 withData:data
    inManagedObjectContext: generalItem.managedObjectContext];
    response.width =width;
    response.height = height;
}


@end
