//
//  ARLAppearDisappearDelegator.m
//  ARLearn
//
//  Created by Stefaan Ternier on 8/7/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLAppearDisappearDelegator.h"

@implementation ARLAppearDisappearDelegator

+ (ARLAppearDisappearDelegator *) sharedSingleton {
    static ARLAppearDisappearDelegator *sharedSingleton;
    static dispatch_once_t predicate;
    dispatch_once(&predicate, ^{
        sharedSingleton = [[ARLAppearDisappearDelegator alloc] init];
    });
    return sharedSingleton;
}

- (void) setTimer: (NSDate *) fireDate {
    NSLog(@"timer is scheduled to go off at %@ ", [fireDate description]);
    
        
        NSTimer *timer = [[NSTimer alloc] initWithFireDate:fireDate
                                                  interval:0.5
                                                    target:self
                                                  selector:@selector(fireTimer)
                                                  userInfo:nil
                                                   repeats:NO];
    
//    [timer fireDate]
    dispatch_async(dispatch_get_main_queue(), ^{
        NSRunLoop *runLoop = [NSRunLoop currentRunLoop];
        [runLoop addTimer:timer forMode:NSDefaultRunLoopMode];
    });
    
}

- (void) fireTimer {
        NSLog(@"timer went off  ");
    dispatch_async(dispatch_get_main_queue(), ^{
        ARLAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
        [CurrentItemVisibility updateVisibility:[[NSUserDefaults standardUserDefaults] objectForKey:@"currentRun"] withManagedContext:appDelegate.managedObjectContext];
        [appDelegate.managedObjectContext save:nil];
        for (CurrentItemVisibility* vis in [CurrentItemVisibility retrieveVisibleFor: [NSNumber numberWithLongLong:3457078]withManagedContext: appDelegate.managedObjectContext]) {
            NSLog(@"vis statement %@", vis.item.name);
        }

        
    });

}

@end
