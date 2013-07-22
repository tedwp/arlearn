//
//  GeneralItemData+Extra.h
//  ARLearn
//
//  Created by Stefaan Ternier on 7/16/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "GeneralItemData.h"

@interface GeneralItemData (Extra)

+ (NSArray *) getUnsyncedData: (NSManagedObjectContext*) context;
+ (void) createDownloadTask: (GeneralItem* ) gi
                    withKey: (NSString*) key
                    withUrl: (NSString*) url
         withManagedContext: (NSManagedObjectContext*) context;
@end
