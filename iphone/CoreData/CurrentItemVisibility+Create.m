//
//  CurrentItemVisibility+Create.m
//  ARLearn
//
//  Created by Stefaan Ternier on 8/6/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "CurrentItemVisibility+Create.h"

@implementation CurrentItemVisibility (Create)

+ (CurrentItemVisibility *) create: (GeneralItem *) generalItem withRun: (Run * ) run   {

    CurrentItemVisibility*  visibility = [NSEntityDescription insertNewObjectForEntityForName:@"CurrentItemVisibility"
                                              inManagedObjectContext:run.managedObjectContext];
    visibility.visible = [NSNumber numberWithBool:NO];
    visibility.item = generalItem;
    visibility.run = run;
    
    return visibility;
}

+ (void) updateVisibility : (NSNumber*) runId withManagedContext: (NSManagedObjectContext*) context{
//    NSNumber* timeDelta = [[NSUserDefaults standardUserDefaults] objectForKey:@"timDelta"];
//    NSNumber * currentTimeMillis = [NSNumber numberWithDouble:(([[NSDate date] timeIntervalSince1970] - timeDelta.longLongValue)* 1000)];
//
//    NSMutableDictionary* appearDict = [[NSMutableDictionary alloc] init];
//    NSMutableDictionary* disappearDict = [[NSMutableDictionary alloc] init];
//    NSMutableDictionary* visibleDict = [[NSMutableDictionary alloc] init];
//    for (GeneralItemVisibility* visibilityStatement in [GeneralItemVisibility retrieve:runId withManagedContext:context]) {
//        if (visibilityStatement.status.intValue == 1) {
//            [appearDict setObject:visibilityStatement forKey:visibilityStatement.generalItemId];
//        }
//        if (visibilityStatement.status.intValue == 2) {
//            [disappearDict setObject:visibilityStatement forKey:visibilityStatement.generalItemId];
//        }
//    }
//    for (NSNumber* key in appearDict) {
//        if (((GeneralItemVisibility*)[appearDict objectForKey:key]).timeStamp.longLongValue < currentTimeMillis.longLongValue) {
//            visibility.visible = [NSNumber numberWithBool:YES];
//        }
//    }
    for (GeneralItem* gi in [GeneralItem retrieve:runId withManagedContext:context]) {
        [self updateVisibility:gi.id runId:runId withManagedContext:context];
    }
}

+ (void) updateVisibility : (NSNumber *) itemId runId:(NSNumber*) runId withManagedContext: (NSManagedObjectContext*) context{
    NSNumber* timeDelta = [[NSUserDefaults standardUserDefaults] objectForKey:@"timDelta"];
    CurrentItemVisibility* visibility = [self retrieve:itemId runId:runId withManagedContext:context];
    double localCurrentTimeMillis = [[NSDate date] timeIntervalSince1970]*1000;
    double currentTimeMillis = localCurrentTimeMillis - timeDelta.longLongValue* 1000;
    GeneralItemVisibility* appearAt;
    GeneralItemVisibility* disAppearAt;
    for (GeneralItemVisibility* visibilityStatement in [GeneralItemVisibility retrieve:itemId runId:runId withManagedContext:context]) {
        if (visibilityStatement.status.intValue == 1) appearAt = visibilityStatement;
        if (visibilityStatement.status.intValue == 2) disAppearAt = visibilityStatement;
    }
    if (appearAt) {
        if (appearAt.timeStamp.doubleValue < currentTimeMillis) {
                visibility.visible = [NSNumber numberWithBool:YES];
        } else {
            NSLog(@"***this item is not yet visible. Set a timer %@", appearAt.generalItem.name);

            double longValue =appearAt.timeStamp.longLongValue - currentTimeMillis;
            NSLog(@"***%f !< %f wait %f milliseconds", appearAt.timeStamp.doubleValue, currentTimeMillis, longValue);
            NSDate* triggerTime = [NSDate dateWithTimeIntervalSince1970:(localCurrentTimeMillis + longValue)/1000];
            
            [[ARLAppearDisappearDelegator sharedSingleton] setTimer:triggerTime];
        }
    }
    if (disAppearAt) {
        if (disAppearAt.timeStamp.doubleValue < currentTimeMillis) {
            visibility.visible = [NSNumber numberWithBool:NO];    
        }else {
            NSLog(@"***this item is not yet invisible. Set a timer");
            
            double longValue =disAppearAt.timeStamp.longLongValue - currentTimeMillis;
            NSLog(@"***%f !< %f wait %f milliseconds", disAppearAt.timeStamp.doubleValue, currentTimeMillis, longValue);
            NSDate* triggerTime = [NSDate dateWithTimeIntervalSince1970:(localCurrentTimeMillis + longValue)/1000];
            
            [[ARLAppearDisappearDelegator sharedSingleton] setTimer:triggerTime];
        }
        
    }
}


+ (CurrentItemVisibility *) retrieve: (NSNumber *) itemId runId:(NSNumber*) runId withManagedContext: (NSManagedObjectContext*) context{
    NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"CurrentItemVisibility"];

    request.predicate = [NSPredicate predicateWithFormat:@"item.id = %lld and run.runId = %lld", [itemId longLongValue], [runId longLongValue]];
    NSError *error = nil;
    
    NSArray *currentItemVisibility = [context executeFetchRequest:request error:&error];
    if (error) {
        NSLog(@"error %@", error);
    }
    if (!currentItemVisibility || ([currentItemVisibility count] != 1)) {
        return nil;
    } else {
        return [currentItemVisibility lastObject];
    }
}

//+ (NSArray *) retrieve: (NSNumber*) runId withManagedContext: (NSManagedObjectContext*) context{
//    NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"CurrentItemVisibility"];
//    request.predicate = [NSPredicate predicateWithFormat:@" run.runId = %lld", [runId longLongValue]];
//    NSError *error = nil;
//    
//    return [context executeFetchRequest:request error:&error];
//    
//}


+ (NSArray *) retrieveVisibleFor: (NSNumber*) runId withManagedContext: (NSManagedObjectContext*) context{
    NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"CurrentItemVisibility"];
    request.predicate = [NSPredicate predicateWithFormat:@"visible = 1 and run.runId = %lld", [runId longLongValue]];
    NSError *error = nil;
    
    return [context executeFetchRequest:request error:&error];
//    if (error) {
//        NSLog(@"error %@", error);
//    }
//    if (!currentItemVisibility || ([currentItemVisibility count] != 1)) {
//        return nil;
//    } else {
//        return [currentItemVisibility lastObject];
//    }
}



@end
