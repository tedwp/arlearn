//
//  ARLBean.h
//  ARLearn
//
//  Created by Stefaan Ternier on 1/15/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ARLBean : NSObject
@property (strong, nonatomic) NSString * type;
@property (strong, nonatomic) NSDictionary * dict;

+ (id) createWithJsonData:(NSData *) jsonData;

+ (id) createWithJsonDictionary:(NSDictionary *) jsonRepresentation;

- (id) initWithJsonDictionary:(NSDictionary *) jsonRepresentation;
@end

@interface ARLRunList : ARLBean
@property (strong, nonatomic) NSMutableDictionary * runs;

@end

@interface ARLRun : ARLBean

@property (strong, nonatomic) NSNumber* runId;
@property (strong, nonatomic) NSNumber* gameId;
@property BOOL deleted;
@property (strong, nonatomic) NSString* title;
@property (strong, nonatomic) NSString* owner;
@end

@interface ARLGeneralItemList : ARLBean
@property (strong, nonatomic) NSMutableDictionary * generalItems;

@end


@interface ARLGeneralItem: ARLBean

@property (strong, nonatomic) NSNumber* gameId;
@property (strong, nonatomic) NSNumber* id;
@property (strong, nonatomic) NSNumber* lat;
@property (strong, nonatomic) NSNumber* lng;
@property (strong, nonatomic) NSNumber* sortKey;
@property BOOL deleted;
@property (strong, nonatomic) NSString* descriptionText;
@property (strong, nonatomic) NSString* richText;
@property (strong, nonatomic) NSString* name;
@end