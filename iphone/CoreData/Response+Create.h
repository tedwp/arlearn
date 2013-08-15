//
//  Response+Create.h
//  ARLearn
//
//  Created by Stefaan Ternier on 7/15/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "Response.h"
#import "Run.h"
#import "GeneralItem.h"
#import "Account.h"
#import "NSString+JSON.h"

@interface Response (Create)

+ (Response *) initResponse: (Run *) run forGeneralItem:(GeneralItem *) gi withValue:(NSString *) value inManagedObjectContext: (NSManagedObjectContext * ) context;
+ (Response *) initResponse: (Run *) run forGeneralItem:(GeneralItem *) gi withData:(NSData *) data inManagedObjectContext:(NSManagedObjectContext * ) context;
+ (void) deleteAll: (NSManagedObjectContext * ) context ;
+ (NSArray *) getUnsyncedReponses: (NSManagedObjectContext*) context;
+ (void) createTextResponse: (NSString *) text withRun: (Run*)run withGeneralItem: (GeneralItem*) generalItem ;
+ (void) createImageResponse:(NSData *) imageUrl width: (NSNumber*) width height: (NSNumber*) height withRun: (Run*)run withGeneralItem: (GeneralItem*) generalItem;
+ (void) createVideoResponse:(NSData *) data
                     withRun: (Run*)run
             withGeneralItem: (GeneralItem*) generalItem;
+ (void) createAudioResponse:(NSData *) data
                     withRun: (Run*)run
             withGeneralItem: (GeneralItem*) generalItem;
@end
