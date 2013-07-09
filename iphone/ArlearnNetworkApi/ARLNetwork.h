//
//  ARLNetwork.h
//  ARLearn
//
//  Created by Stefaan Ternier on 1/25/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <Foundation/Foundation.h>
#define serviceUrl @"http://ar-learn.appspot.com/rest/"
//#define serviceUrl @"http://192.168.1.6:9999/rest/"
#define textplain @"text/plain"

#define accept @"Accept"
#define contenttype @"Content-Type"
#define applicationjson @"application/json"
#define textplain @"text/plain"
#define GET @"GET"
#define POST @"POST"

//#define myRunsPostfix @"myRuns"

@interface ARLNetwork : NSObject

+ (NSString*) requestAuthToken: (NSString *) username password: (NSString *) password ;

+ (NSDictionary*) runsParticipate ;
+ (NSDictionary*) runsParticipateFrom: (NSNumber *) from;
+ (NSDictionary*) runsWithId: (NSNumber *) id;

+ (NSDictionary*) gamesParticipate;
+ (NSDictionary*) gamesParticipateFrom: (NSNumber *) from;

+ (NSDictionary*) itemsForRun: (int64_t) runId;
+ (NSDictionary*) itemsForGameFrom: (NSNumber *) gameId from:(NSNumber *) from;

+ (NSDictionary *) itemVisibilityForRun: (NSNumber *) runId;
+ (NSDictionary*) itemVisibilityForRun: (NSNumber *) runId from: (NSNumber *) from ;

+ (void) registerDevice: (NSString *) token withUID: (NSString *) deviceUID withAccount: (NSString *) email;

+ (void) publishAction: (NSDictionary *) actionDict;
+ (void) publishAction: (long) runId action: (NSString *) action itemId: (long) itemId itemType:(NSString *) itemType;


+ (NSDictionary*) anonymousLogin: (NSString *) account ;
+ (NSDictionary*) accountDetails;

@end


