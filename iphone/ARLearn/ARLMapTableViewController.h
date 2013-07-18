//
//  ARLMapTableViewController.h
//  ARLearn
//
//  Created by Stefaan Ternier on 2/5/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>


@interface ARLMapTableViewController : UIViewController <NSFetchedResultsControllerDelegate>

@property (strong, nonatomic) NSFetchedResultsController *fetchedResultsController;
@property BOOL debug;

- (void)performFetch;

@end
