//
//  ARLMyGamesViewController.m
//  ARLearn
//
//  Created by Stefaan Ternier on 8/13/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLMyGamesViewController.h"

@interface ARLMyGamesViewController ()

@end

@implementation ARLMyGamesViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void) fetchARLearnGames: (NSManagedObjectContext *)managedObjectContext {
    
    NSString *authToken = [[NSUserDefaults standardUserDefaults] objectForKey:@"auth"];
    if (authToken) {
        ARLCloudSynchronizer* synchronizer = [[ARLCloudSynchronizer alloc] init];
        [synchronizer createContext:managedObjectContext];
        
        synchronizer.syncGames = YES;
        [synchronizer sync];
    }
}

- (void) setupFetchedResultsController {
    ARLAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
    
    NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"Game"];
    
//    request.predicate = [NSPredicate predicateWithFormat: @"deleted = %d", NO];
    
    request.sortDescriptors =[NSArray arrayWithObject:[NSSortDescriptor sortDescriptorWithKey:@"title" ascending:YES selector:@selector(localizedCaseInsensitiveCompare:)]];
    
    self.fetchedResultsController = [[NSFetchedResultsController alloc] initWithFetchRequest:request
                                                                        managedObjectContext:appDelegate.managedObjectContext
                                                                          sectionNameKeyPath:nil cacheName:nil];
    //    }
}

- (void) viewDidLoad {
    [super viewDidLoad];
    self.navigationItem.title = @"test";
    ARLAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
    [self fetchARLearnGames: appDelegate.managedObjectContext];
    [self setupFetchedResultsController];
    self.title = NSLocalizedString(@"MyOwnGames", nil);
}

- (void) viewDidAppear:(BOOL)animated {
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"myGameCellIdentifier";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc]
                initWithStyle:UITableViewCellStyleDefault
                reuseIdentifier:CellIdentifier];
    }
    Game* g = [self.fetchedResultsController objectAtIndexPath:indexPath];
    cell.textLabel.text = g.title;
    return cell;
}

-(void) configureCell: (UITableViewCell *) cell atIndexPath:(NSIndexPath *)indexPath {
    Game* g = [self.fetchedResultsController objectAtIndexPath:indexPath];
    cell.textLabel.text = g.title;
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    NSIndexPath *indexPath = [self.tableView indexPathForSelectedRow];
    
    Game * game = [self.fetchedResultsController objectAtIndexPath:indexPath];
    if ([segue.destinationViewController respondsToSelector:@selector(setGame:)]) {
        [segue.destinationViewController performSelector:@selector(setGame:) withObject:game];
    }
    
}





@end
