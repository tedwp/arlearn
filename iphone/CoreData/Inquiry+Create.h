//
//  Inquiry+Create.h
//  PersonalInquiryManager
//
//  Created by Stefaan Ternier on 8/8/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "Inquiry.h"

@interface Inquiry (Create)

+ (Inquiry *) inquirytWithDictionary: (NSDictionary *) inquiryDict inManagedObjectContext: (NSManagedObjectContext * ) context ;
+ (Inquiry *) retrieveFromDb: (NSDictionary *) inqDict withManagedContext: (NSManagedObjectContext*) context;
@end
