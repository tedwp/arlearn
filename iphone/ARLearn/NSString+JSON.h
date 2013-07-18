//
//  NSString+JSON.h
//  ARLearn
//
//  Created by Stefaan Ternier on 7/17/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSString (JSON)

+ (NSString*) jsonString:(NSDictionary *) jsonDictionary;

@end
