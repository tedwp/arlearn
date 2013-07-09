//
//  ARLNetwork.m
//  ARLearn
//
//  Created by Stefaan Ternier on 1/12/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLNetwork.h"


@implementation ARLNetwork

+ (NSMutableURLRequest *) prepareRequest: (NSString *) method requestWithUrl: (NSString *) url {
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString: url]
                                                           cachePolicy:NSURLRequestReloadIgnoringLocalCacheData
                                                       timeoutInterval:60.0];
    [request setHTTPMethod:method];
    [request setValue:applicationjson forHTTPHeaderField:accept];
    return request;
}

+ (id) executeARLearnGetWithAuthorization: (NSString *) path {
    NSString* urlString = [NSString stringWithFormat:@"%@%@", serviceUrl, path];
    NSMutableURLRequest *request = [self prepareRequest:@"GET" requestWithUrl:urlString];
    
    NSString * authorizationString = [NSString stringWithFormat:@"GoogleLogin auth=%@", [[NSUserDefaults standardUserDefaults] objectForKey:@"auth"]];
    [request setValue:authorizationString forHTTPHeaderField:@"Authorization"];
    
    NSData *jsonData = [ NSURLConnection sendSynchronousRequest:request returningResponse: nil error: nil ];
    NSError *error = nil;
    return jsonData ? [NSJSONSerialization JSONObjectWithData:jsonData options:NSJSONReadingMutableContainers|NSJSONReadingMutableLeaves error:&error] : nil;
}

+ (id) executeARLearnPostWithAuthorization: (NSString *) path postData:(NSData *) data withContentType: (NSString *) ctValue{
    NSString* urlString = [NSString stringWithFormat:@"%@%@", serviceUrl, path];
    NSMutableURLRequest *request = [self prepareRequest:@"POST" requestWithUrl:urlString];
    
    NSString * authorizationString = [NSString stringWithFormat:@"GoogleLogin auth=%@", [[NSUserDefaults standardUserDefaults] objectForKey:@"auth"]];
    [request setValue:authorizationString forHTTPHeaderField:@"Authorization"];
    
    [request setHTTPBody:data];
    if (ctValue) [request setValue:ctValue forHTTPHeaderField:contenttype];

    NSData *jsonData = [ NSURLConnection sendSynchronousRequest:request returningResponse: nil error: nil ];
    NSError *error = nil;
    return jsonData ? [NSJSONSerialization JSONObjectWithData:jsonData options:NSJSONReadingMutableContainers|NSJSONReadingMutableLeaves error:&error] : nil;
}

+ (id) executeARLearnGet: (NSString *) path {
    NSString* urlString = [NSString stringWithFormat:@"%@%@", serviceUrl, path];
    NSMutableURLRequest *request = [self prepareRequest:@"GET" requestWithUrl:urlString];
    NSData *jsonData = [ NSURLConnection sendSynchronousRequest:request returningResponse: nil error: nil ];
    NSError *error = nil;
    return jsonData ? [NSJSONSerialization JSONObjectWithData:jsonData options:NSJSONReadingMutableContainers|NSJSONReadingMutableLeaves error:&error] : nil;
}

+ (id) executeARLearnPOST: (NSString *) path
                 postData:(NSData *) data
               withAccept: (NSString *) acceptValue
          withContentType: (NSString *) ctValue{
    NSString* urlString = [NSString stringWithFormat:@"%@%@", serviceUrl, path];
    NSMutableURLRequest *request = [self prepareRequest:@"POST" requestWithUrl:urlString];

    [request setHTTPBody:data];
    if (ctValue) [request setValue:ctValue forHTTPHeaderField:contenttype];
    if (acceptValue) [request setValue:acceptValue forHTTPHeaderField:accept];
    NSData *jsonData = [ NSURLConnection sendSynchronousRequest:request returningResponse: nil error: nil ];
    NSError *error = nil;
    return jsonData ? [NSJSONSerialization JSONObjectWithData:jsonData options:NSJSONReadingMutableContainers|NSJSONReadingMutableLeaves error:&error] : nil;
}

+ (NSData *) stringToData: (NSString * ) string {
    const char *utf8String = [string UTF8String];
    return [NSData dataWithBytes:utf8String length:strlen(utf8String)];
}

//Authentication
+ (NSString*) requestAuthToken: (NSString *) username password: (NSString *) password {
    NSData * postData = [self stringToData:[NSString stringWithFormat:@"%@\n%@", username, password]];
    return [[self executeARLearnPostWithAuthorization:@"login" postData:postData withContentType:textplain] objectForKey:@"auth"];
    
}

//Runs
+ (NSDictionary*) runsParticipate {
    return [self executeARLearnGetWithAuthorization:@"myRuns/participate"];
}

+ (NSDictionary*) runsParticipateFrom: (NSNumber *) from{
    return [self executeARLearnGetWithAuthorization:[NSString stringWithFormat:@"myRuns/participate?from=%lld", [from longLongValue]]];
}

+ (NSDictionary*) runsWithId: (NSNumber *) id{
    return [self executeARLearnGetWithAuthorization:[NSString stringWithFormat:@"myRuns/runId/%lld", [id longLongValue]]];
}

//Games
+ (NSDictionary*) gamesParticipate {
    return [self executeARLearnGetWithAuthorization:@"myGames/participate"];
}

+ (NSDictionary*) gamesParticipateFrom: (NSNumber *) from{
    return [self executeARLearnGetWithAuthorization:[NSString stringWithFormat:@"myGames/participate?from=%lld", [from longLongValue]]];
}
//GeneralItems
+ (NSDictionary*) itemsForRun: (int64_t) runId{
    return [self executeARLearnGetWithAuthorization:[NSString stringWithFormat:@"generalItems/runId/%lld", runId ]];
}

+ (NSDictionary*) itemsForGameFrom: (NSNumber *) gameId from:(NSNumber *) from{
    return [self executeARLearnGetWithAuthorization:[NSString stringWithFormat:@"generalItems/gameId/%lld?from=%lld", [gameId longLongValue],[from longLongValue] ]];
}

+ (NSDictionary *) itemVisibilityForRun: (NSNumber *) runId{
    return [self executeARLearnGetWithAuthorization:[NSString stringWithFormat:@"generalItemsVisibility/runId/%lld", [runId longLongValue]]];
}

+ (NSDictionary *) itemVisibilityForRun: (NSNumber *) runId from: (NSNumber *) from{
    return [self executeARLearnGetWithAuthorization:[NSString stringWithFormat:@"generalItemsVisibility/runId/%lld?from=%lld", [runId longLongValue], [from longLongValue]]];
}

//APN

+ (void) registerDevice: (NSString *) token withUID: (NSString *) deviceUID withAccount: (NSString *) email {
    if (!email) return;
    NSDictionary *tmp = [[NSDictionary alloc] initWithObjectsAndKeys:
                         email, @"account",
                         deviceUID, @"deviceUniqueIdentifier",
                         token, @"deviceToken",
                         nil];
    NSData *postData = [NSJSONSerialization dataWithJSONObject:tmp options:0 error:nil];
    [self executeARLearnPOST:@"apn" postData:postData withAccept:nil withContentType:applicationjson ];
    
}

//Actions

+ (void) publishAction: (NSDictionary *) actionDict {
    NSData *postData = [NSJSONSerialization dataWithJSONObject:actionDict options:0 error:nil];
    [self executeARLearnPOST:@"actions"
                    postData:postData
                  withAccept:applicationjson
             withContentType:applicationjson];
}

+ (void) publishAction: (long) runId
                    action: (NSString *) action
                    itemId: (long) itemId
                  itemType:(NSString *) itemType {
    NSDictionary *actionDict = [[NSDictionary alloc] initWithObjectsAndKeys:
                                action, @"action",
                                [NSNumber numberWithLong:runId], @"runId",
                                [NSNumber numberWithLong:itemId], @"generalItemId",
                                [[NSUserDefaults standardUserDefaults] objectForKey:@"username"], @"userEmail",
                                itemType, @"generalItemType",
                                nil];
    NSLog(@"publish action%@", actionDict);
    [self publishAction:actionDict];
}

// Account

+ (NSDictionary*) anonymousLogin: (NSString *) account {
    return [self executeARLearnGet:[NSString stringWithFormat:@"account/anonymousLogin/%@", account]];
}

+ (NSDictionary*) accountDetails {
    return [self executeARLearnGetWithAuthorization:[NSString stringWithFormat:@"account/accountDetails"]];
}

@end
