//
//  Account+create.m
//  ARLearn
//
//  Created by Stefaan Ternier on 7/8/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "Account+create.h"

@implementation Account (create)


+ (Account *) accountWithDictionary: (NSDictionary *) acDict inManagedObjectContext: (NSManagedObjectContext * ) context {
    Account * account = [self retrieveFromDb:acDict withManagedContext:context];
    if (!account) {
        account = [NSEntityDescription insertNewObjectForEntityForName:@"Account" inManagedObjectContext:context];
    }
    account.localId = [acDict objectForKey:@"localId"];
    account.accountType = [acDict objectForKey:@"accountType"];
    
    account.email = [acDict objectForKey:@"email"];
    account.name= [acDict objectForKey:@"name"];
    account.givenName = [acDict objectForKey:@"givenName"];
    account.familyName = [acDict objectForKey:@"familyName"];
    account.accountLevel= [acDict objectForKey:@"accountLevel"];
    NSURL  *url = [NSURL URLWithString:[acDict objectForKey:@"picture"]];
    NSData *urlData = [NSData dataWithContentsOfURL:url];
    if ( urlData ){
        account.picture = urlData;
    }

    NSError * error;
    
    if (![context save:&error]) {
        NSLog(@"error %@", error);
    }
    return account;
}




+ (Account *) retrieveFromDb: (NSDictionary *) giDict withManagedContext: (NSManagedObjectContext*) context{
    Account * account = nil;
    
    NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"Account"];
    NSLog(@"(localId = %@) AND (accountType = %d)", [giDict objectForKey:@"localId"], [[giDict objectForKey:@"accountType"] intValue]);
    request.predicate = [NSPredicate predicateWithFormat:@"(localId = %@) AND (accountType = %d)", [giDict objectForKey:@"localId"], [[giDict objectForKey:@"accountType"] intValue]];
    NSArray *accountsFromDb = [context executeFetchRequest:request error:nil];
    if (!accountsFromDb || ([accountsFromDb count] != 1)) {
        return nil;
    } else {
        account = [accountsFromDb lastObject];
        return account;
    }
    
}

+ (Account *) retrieveFromDbWithLocalId: (NSString *) localId withManagedContext: (NSManagedObjectContext*) context{
    Account * account = nil;
    
    NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"Account"];
    NSLog(@"(localId = %@)", localId);
    request.predicate = [NSPredicate predicateWithFormat:@"(localId = %@) ", localId];
    NSArray *accountsFromDb = [context executeFetchRequest:request error:nil];
    if (!accountsFromDb || ([accountsFromDb count] != 1)) {
        return nil;
    } else {
        account = [accountsFromDb lastObject];
        return account;
    }
    
}


@end
