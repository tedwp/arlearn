//
//  ARLAppDelegate.m
//  ARLearn
//
//  Created by Stefaan Ternier on 1/11/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLAppDelegate.h"
#import "ARLNotificationSubscriber.h"

@implementation ARLAppDelegate

@synthesize arlearnDatabase = _arlearnDatabase;


- (void) setArlearnDatabase:(UIManagedDocument *)arlearnDatabase {
    if (_arlearnDatabase != arlearnDatabase) {
        _arlearnDatabase = arlearnDatabase;
//        [self useDocument];
    }
}




- (NSURL *)applicationDocumentsDirectory {
    return [[[NSFileManager defaultManager] URLsForDirectory:NSDocumentDirectory inDomains:NSUserDomainMask] lastObject];
}

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    // Override point for customization after application launch.
    
    if (!self.arlearnDatabase) { //create the database if one does not exist
        NSURL *url = [[[NSFileManager defaultManager] URLsForDirectory:NSDocumentDirectory inDomains:NSUserDomainMask] lastObject];
        url = [url URLByAppendingPathComponent:@"ARLearn Database"];
        self.arlearnDatabase = [[UIManagedDocument alloc] initWithFileURL:url];
    }
    
    [[UIApplication sharedApplication] registerForRemoteNotificationTypes: (UIRemoteNotificationTypeBadge | UIRemoteNotificationTypeSound | UIRemoteNotificationTypeAlert)];
    
    if (launchOptions != nil)
	{
		NSDictionary* dictionary = [launchOptions objectForKey:UIApplicationLaunchOptionsRemoteNotificationKey];
		if (dictionary != nil)
		{
			NSLog(@"Launched from push notification: %@", dictionary);
//			[self addMessageFromRemoteNotification:dictionary updateUI:NO];
		}
	}
    
    return YES;
}

- (void)application:(UIApplication*)application didReceiveRemoteNotification:(NSDictionary*)message
{
	NSLog(@"Received notification: %@", message);
    [[ARLNotificationSubscriber sharedSingleton] dispatchMessage:message];
//	[self addMessageFromRemoteNotification:userInfo updateUI:YES];
}



- (void)application:(UIApplication*)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData*)deviceToken
{
	NSLog(@"My token is: %@", deviceToken);
    
    NSString* newToken = [deviceToken description];
	newToken = [newToken stringByTrimmingCharactersInSet:[NSCharacterSet characterSetWithCharactersInString:@"<>"]];
	newToken = [newToken stringByReplacingOccurrencesOfString:@" " withString:@""];
    NSLog(@"My token is: %@", newToken);
    UIDevice *device = [UIDevice currentDevice];
    NSString *uniqueIdentifier = [device uniqueIdentifier];
        NSLog(@"My device id: %@", uniqueIdentifier);
    NSString * email = [[NSUserDefaults standardUserDefaults] objectForKey:@"username"];

    [ARLNetwork registerDevice:newToken withUID:uniqueIdentifier withAccount:email];
//    [[[ARLNetworkAPN alloc] init] registerDevice:newToken withUID:uniqueIdentifier];

    
}

- (void)application:(UIApplication*)application didFailToRegisterForRemoteNotificationsWithError:(NSError*)error
{
	NSLog(@"Failed to get token, error: %@", error);
}
							
- (void)applicationWillResignActive:(UIApplication *)application {
}

- (void)applicationDidEnterBackground:(UIApplication *)application {
}

- (void)applicationWillEnterForeground:(UIApplication *)application {

}

- (void)applicationDidBecomeActive:(UIApplication *)application {

}

- (void)applicationWillTerminate:(UIApplication *)application {

}

@end
