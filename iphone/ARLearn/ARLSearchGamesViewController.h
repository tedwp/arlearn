//
//  ARLSearchGamesViewController.h
//  ARLearn
//
//  Created by Stefaan Ternier on 8/12/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ARLNetwork.h"
#import "Game+ARLearnBeanCreate.h"
#import "ARLAppDelegate.h"

@interface ARLSearchGamesViewController : UITableViewController <UISearchBarDelegate, UISearchDisplayDelegate>

@property IBOutlet UISearchBar *searchBar;

@property (nonatomic, strong) NSArray * searchArray;

@end
