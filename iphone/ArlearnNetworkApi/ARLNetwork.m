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
    NSString* urlString = [NSString stringWithFormat:@"%@/rest/%@", serviceUrl, path];
    NSMutableURLRequest *request = [self prepareRequest:@"GET" requestWithUrl:urlString];
    
    NSString * authorizationString = [NSString stringWithFormat:@"GoogleLogin auth=%@", [[NSUserDefaults standardUserDefaults] objectForKey:@"auth"]];
    [request setValue:authorizationString forHTTPHeaderField:@"Authorization"];
    
    NSData *jsonData = [ NSURLConnection sendSynchronousRequest:request returningResponse: nil error: nil ];
    NSError *error = nil;
    return jsonData ? [NSJSONSerialization JSONObjectWithData:jsonData options:NSJSONReadingMutableContainers|NSJSONReadingMutableLeaves error:&error] : nil;
}

+ (id) executeARLearnPostWithAuthorization: (NSString *) path postData:(NSData *) data withContentType: (NSString *) ctValue{
    NSString* urlString = [NSString stringWithFormat:@"%@/rest/%@", serviceUrl, path];
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
    NSString* urlString = [NSString stringWithFormat:@"%@/rest/%@", serviceUrl, path];
    NSMutableURLRequest *request = [self prepareRequest:@"GET" requestWithUrl:urlString];
    NSData *jsonData = [ NSURLConnection sendSynchronousRequest:request returningResponse: nil error: nil ];
    NSError *error = nil;
    return jsonData ? [NSJSONSerialization JSONObjectWithData:jsonData options:NSJSONReadingMutableContainers|NSJSONReadingMutableLeaves error:&error] : nil;
}

+ (id) executeARLearnPOST: (NSString *) path
                 postData:(NSData *) data
               withAccept: (NSString *) acceptValue
          withContentType: (NSString *) ctValue{
    NSString* urlString;
    if ([path hasPrefix:@"/"]) {
        urlString = [NSString stringWithFormat:@"%@%@", serviceUrl, path];
    } else {
        urlString = [NSString stringWithFormat:@"%@/rest/%@", serviceUrl, path];

    }
    NSMutableURLRequest *request = [self prepareRequest:@"POST" requestWithUrl:urlString];

    [request setHTTPBody:data];
    if (ctValue) [request setValue:ctValue forHTTPHeaderField:contenttype];
    if (acceptValue) [request setValue:acceptValue forHTTPHeaderField:accept];
    NSData *jsonData = [ NSURLConnection sendSynchronousRequest:request returningResponse: nil error: nil ];
    NSError *error = nil;
    if ([acceptValue isEqualToString:textplain]) {
        return [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
        //return [NSString stringWithUTF8String:[jsonData bytes]];
    }
    return jsonData ? [NSJSONSerialization JSONObjectWithData:jsonData options:NSJSONReadingMutableContainers|NSJSONReadingMutableLeaves error:&error] : @"returnin gsth";
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

//Response

+ (void) publishResponse: (NSDictionary *) responseDict {
    NSData *postData = [NSJSONSerialization dataWithJSONObject:responseDict options:0 error:nil];
    id jsonData =[self executeARLearnPostWithAuthorization:@"response" postData:postData withContentType:applicationjson];
//    id jsonData =     [self executeARLearnPOST:@"response"
//                    postData:postData
//                  withAccept:applicationjson
//             withContentType:applicationjson];
    NSLog(@"return server %@", jsonData);
}

+ (void) publishResponse: (NSNumber *) runId
           responseValue: (NSString *) value
                  itemId: (NSNumber*) generalItemId
               timeStamp: (NSNumber*) timeStamp{
    NSString* accountType = [[NSUserDefaults standardUserDefaults] objectForKey:@"accountType"];
    NSString* accountLocalId = [[NSUserDefaults standardUserDefaults] objectForKey:@"accountLocalId"];
    NSString* account = [NSString stringWithFormat:@"%@:%@", accountType, accountLocalId];
    NSDictionary *responseDict = [[NSDictionary alloc] initWithObjectsAndKeys:
                                value, @"responseValue",
                                runId, @"runId",
                                generalItemId, @"generalItemId",
                                timeStamp, @"timestamp",
                                account, @"userEmail",
                                nil];
    NSLog(@"publish response%@", responseDict);
[self publishResponse:responseDict];
    
}

//File upload

+ (NSString*) requestUploadUrl: (NSString*) fileName withRun:(NSNumber *) runId {
    NSString * str =[NSString stringWithFormat:@"runId=%@&account=%@:%@&fileName=%@", runId,
                     [[NSUserDefaults standardUserDefaults] objectForKey:@"accountType"],
                     [[NSUserDefaults standardUserDefaults] objectForKey:@"accountLocalId"],fileName];
//    str = @"runId=3&account=2:116743449349920850150&fileName=testImage";
    id response = [self executeARLearnPOST:[NSString stringWithFormat: @"/uploadServiceWithUrl"]
                    postData:[str dataUsingEncoding:NSUTF8StringEncoding]
                  withAccept:textplain
             withContentType:xwwformurlencode];
    NSLog(@"return response %@", response);
    return (NSString*) response;
}
+ (void) perfomUpload: (NSString*) uploadUrl withFileName:(NSString*) fileName
          contentType:(NSString*) contentTypeIn withData:(NSData*) data {
    NSString *boundary = @"0xKhTmLbOuNdArY";
    
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
    [request setCachePolicy:NSURLRequestReloadIgnoringLocalCacheData];
    [request setHTTPShouldHandleCookies:NO];
    [request setTimeoutInterval:30];
    [request setHTTPMethod:@"POST"];
    
    // set Content-Type in HTTP header
    NSString *contentType = [NSString stringWithFormat:@"multipart/form-data; boundary=%@", boundary];
    [request setValue:contentType forHTTPHeaderField: @"Content-Type"];
    
    // post body
    NSMutableData *body = [NSMutableData data];
    
    if (data) {
        [body appendData:[[NSString stringWithFormat:@"--%@\r\n", boundary] dataUsingEncoding:NSUTF8StringEncoding]];
        [body appendData:[[NSString stringWithFormat:@"Content-Disposition: form-data; name=\"uploaded_file\"; filename=\"%@\"\r\n", fileName] dataUsingEncoding:NSUTF8StringEncoding]];
        [body appendData:[[NSString stringWithFormat:@"Content-Type: %@\r\n", contentTypeIn] dataUsingEncoding:NSUTF8StringEncoding]];
        [body appendData:[@"Content-Transfer-Encoding: binary\r\n\r\n" dataUsingEncoding:NSUTF8StringEncoding]];
        //        [body appendData:[[NSString stringWithString:@"Content-Type: %@\r\n\r\n", contentTypeIn] dataUsingEncoding:NSUTF8StringEncoding]];
        [body appendData:data];
        [body appendData:[[NSString stringWithFormat:@"\r\n"] dataUsingEncoding:NSUTF8StringEncoding]];
    }
    
    [body appendData:[[NSString stringWithFormat:@"--%@--\r\n", boundary] dataUsingEncoding:NSUTF8StringEncoding]];
    
    // setting the body of the post to the reqeust
    [request setHTTPBody:body];

//        NSLog(@"body looks like %@ ",[NSString stringWithUTF8String:[body bytes]]);
    uploadUrl = [uploadUrl stringByReplacingOccurrencesOfString:@"localhost:8888" withString:@"192.168.1.8:8080"];
        NSLog(@"uploadUrl looks like*** %@ ***stop",uploadUrl);
    // set URL
    [request setURL:[NSURL URLWithString: uploadUrl]];
    
    NSData *jsonData = [ NSURLConnection sendSynchronousRequest:request returningResponse: nil error: nil ];

    NSLog(@"return from server %@ ***",[NSString stringWithUTF8String:[jsonData bytes]]);

}

// Account

+ (NSDictionary*) anonymousLogin: (NSString *) account {
    return [self executeARLearnGet:[NSString stringWithFormat:@"account/anonymousLogin/%@", account]];
}

+ (NSDictionary*) accountDetails {
    return [self executeARLearnGetWithAuthorization:[NSString stringWithFormat:@"account/accountDetails"]];
}

@end
