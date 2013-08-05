//
//  NSString+JSON.m
//  ARLearn
//
//  Created by Stefaan Ternier on 7/17/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "NSString+JSON.h"

@implementation NSString (JSON)

+ (NSString*) jsonString:(NSDictionary *) jsonDictionary {
    NSError *error;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:jsonDictionary
                                                       options:0
                                                         error:&error];
    return [[NSString alloc] initWithBytes:[jsonData bytes] length:[jsonData length] encoding:NSUTF8StringEncoding];
}

@end
