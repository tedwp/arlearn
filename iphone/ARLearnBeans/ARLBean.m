//
//  ARLBean.m
//  ARLearn
//
//  Created by Stefaan Ternier on 1/15/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLBean.h"

@implementation ARLBean: NSObject
@synthesize type = _type;
@synthesize dict = _dict;

+ (id) createWithJsonData:(NSData *) jsonData {
    NSError* error;
    NSDictionary* json = [NSJSONSerialization JSONObjectWithData:jsonData
                                                         options:kNilOptions
                                                           error:&error];
//    NSLog(@"json %@", json);
    return [ARLBean createWithJsonDictionary:json];
}

+ (id) createWithJsonDictionary:(NSDictionary *) jsonRepresentation {
    
    NSString * type = [jsonRepresentation objectForKey:@"type"];
    ARLBean * returnBean ;
    if ([type isEqualToString:@"org.celstec.arlearn2.beans.run.RunList"]) {
        returnBean = [[ARLRunList alloc] initWithJsonDictionary: jsonRepresentation];
        returnBean.type = type;
    }
    if ([type isEqualToString:@"org.celstec.arlearn2.beans.run.Run"]) {
        returnBean = [[ARLRun alloc] initWithJsonDictionary: jsonRepresentation];
        returnBean.type = type;
    }
    if ([type isEqualToString:@"org.celstec.arlearn2.beans.generalItem.GeneralItemList"]) {
        returnBean = [[ARLGeneralItemList alloc] initWithJsonDictionary: jsonRepresentation];
        returnBean.type = type;
    }
    if ([type isEqualToString:@"org.celstec.arlearn2.beans.generalItem.NarratorItem"]) {
        returnBean = [[ARLGeneralItem alloc] initWithJsonDictionary: jsonRepresentation];
        returnBean.type = type;
    }
    if ([type isEqualToString:@"org.celstec.arlearn2.beans.generalItem.AudioObject"]) {
        returnBean = [[ARLGeneralItem alloc] initWithJsonDictionary: jsonRepresentation];
        returnBean.type = type;
    }
    if ([type isEqualToString:@"org.celstec.arlearn2.beans.generalItem.MultipleChoiceTest"]) {
        returnBean = [[ARLGeneralItem alloc] initWithJsonDictionary: jsonRepresentation];
        returnBean.type = type;
    }
    if ([type isEqualToString:@"org.celstec.arlearn2.beans.generalItem.ScanTag"]) {
        returnBean = [[ARLGeneralItem alloc] initWithJsonDictionary: jsonRepresentation];
        returnBean.type = type;
    }
    return returnBean;
}

- (id) initWithJsonDictionary:(NSDictionary *) jsonRepresentation {
    self = [self init];
    self.type = [jsonRepresentation objectForKey:@"type"];
    self.dict = jsonRepresentation;
    return self;
}

- (NSString *) description {
    return [NSString stringWithFormat:@"(type=%@)", self.type];
}


@end



@implementation ARLRunList

@synthesize runs = _runs;

- (NSMutableDictionary *) runs{
    if (!_runs) {
        _runs = [[NSMutableDictionary alloc] init];
    }
    return _runs;
}

- (id) initWithJsonDictionary:(NSDictionary *) jsonRepresentation {
    self = [super initWithJsonDictionary:jsonRepresentation];
    NSDictionary * runsDict = [jsonRepresentation objectForKey:@"runs"];
    for (NSDictionary *run in runsDict) {
        ARLRun * arlRun = [ARLBean createWithJsonDictionary:run];
        [self.runs setObject:arlRun forKey:arlRun.runId];
    }
    return self;
}

- (NSString *) description {
    return [NSString stringWithFormat:@"(type=%@ firstrun= %@)", self.type,[self.runs objectForKey:[NSNumber numberWithLong:543040]]];
}

@end

@implementation ARLRun

@synthesize runId = _runId;
@synthesize gameId = _gameId;
@synthesize deleted = _deleted;
@synthesize title = _title;
@synthesize owner = _owner;

- (id) initWithJsonDictionary:(NSDictionary *) jsonRepresentation {
    self = [super initWithJsonDictionary:jsonRepresentation];
    self.runId = [jsonRepresentation objectForKey:@"runId"];
    self.gameId = [jsonRepresentation objectForKey:@"gameId"];
    self.deleted = [[jsonRepresentation objectForKey:@"deleted"] boolValue];
    self.title = [jsonRepresentation objectForKey:@"title"];
    self.owner = [jsonRepresentation objectForKey:@"owner"];
    return self;
}

- (NSString *) description {
    return [NSString stringWithFormat:@"(type=%@, runId=%ld, gameId=%ld, deleted=%hhd, title=%@ , owner=%@)", self.type, [self.runId longValue], [self.gameId longValue], self.deleted, self.title, self.owner];
}

@end

@implementation ARLGeneralItemList

@synthesize generalItems = _generalItems;

- (NSMutableDictionary *) generalItems{
    if (!_generalItems) {
        _generalItems = [[NSMutableDictionary alloc] init];
    }
    return _generalItems;
}

- (id) initWithJsonDictionary:(NSDictionary *) jsonRepresentation {
    self = [super initWithJsonDictionary:jsonRepresentation];
    NSDictionary * giDict = [jsonRepresentation objectForKey:@"generalItems"];
    for (NSDictionary *gi in giDict) {
        ARLGeneralItem * generalItem = [ARLBean createWithJsonDictionary:gi];
        if (generalItem) {
         [self.generalItems setObject:generalItem forKey:generalItem.id];
        }
    }
    return self;
}

- (NSString *) description {
    return [NSString stringWithFormat:@"(type=%@ firstItem= )", self.type];
}

@end

@implementation ARLGeneralItem

@synthesize gameId = _gameId;
@synthesize id = _id;
@synthesize sortKey = _sortKey;
@synthesize lat = _lat;
@synthesize lng = _lng;
@synthesize deleted = _deleted;
@synthesize richText = _richText;
@synthesize descriptionText = _descriptionText;
@synthesize name = _name;

- (id) initWithJsonDictionary:(NSDictionary *) jsonRepresentation {
    self = [super initWithJsonDictionary:jsonRepresentation];
    self.id = [jsonRepresentation objectForKey:@"id"];
    self.gameId = [jsonRepresentation objectForKey:@"gameId"];
    self.sortKey = [jsonRepresentation objectForKey:@"sortKey"];
    self.deleted = [[jsonRepresentation objectForKey:@"deleted"] boolValue];

    self.descriptionText = [jsonRepresentation objectForKey:@"description"];
    self.richText = [jsonRepresentation objectForKey:@"richText"];
    self.name = [jsonRepresentation objectForKey:@"name"];
    self.lat = [jsonRepresentation objectForKey:@"lat"];
    self.lng = [jsonRepresentation objectForKey:@"lng"];
    
    return self;
}

- (NSString *) description {
    return [NSString stringWithFormat:@"(type=%@, id=%ld, gameId=%ld, deleted=%hhd, name=%@ )", self.type, [self.id longValue], [self.gameId longValue], self.deleted, self.name];
}

@end
