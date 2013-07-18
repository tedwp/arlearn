//
//  ARLAppDelegate.h
//  ARLearn
//
//  Created by Stefaan Ternier on 1/11/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreData/CoreData.h>

#import "ARLNetwork.h"
#import "Run+ARLearnBeanCreate.h"
#import "Game+ARLearnBeanCreate.h"
#import "ARLCloudSynchronizer.h"


@interface ARLAppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;

@property (readonly, strong, nonatomic) NSManagedObjectContext *managedObjectContext;
@property (readonly, strong, nonatomic) NSManagedObjectModel *managedObjectModel;
@property (readonly, strong, nonatomic) NSPersistentStoreCoordinator *persistentStoreCoordinator;

//@property (nonatomic, strong, readonly) NSManagedObjectContext *managedObjectContext;
//@property (nonatomic, strong) UIManagedDocument *arlearnDatabase;


@end
