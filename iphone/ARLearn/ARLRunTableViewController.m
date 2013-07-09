//
//  ARLRunTableViewController.m
//  ARLearn
//
//  Created by Stefaan Ternier on 1/13/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLRunTableViewController.h"

@interface ARLRunTableViewController ()

@end

@implementation ARLRunTableViewController

//@synthesize arlearnDatabase = _arlearnDatabase;


- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}

-(void) useDocument {
    ARLAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
    
    if (![[NSFileManager defaultManager] fileExistsAtPath:[appDelegate.arlearnDatabase.fileURL path]]) {
        [appDelegate.arlearnDatabase saveToURL:appDelegate.arlearnDatabase.fileURL forSaveOperation:UIDocumentSaveForCreating completionHandler:^(BOOL succes) {
            [self fetchARLearnGamesAndRuns: appDelegate.arlearnDatabase];
            [self setupFetchedResultsController];
        }];
    } else if (appDelegate.arlearnDatabase.documentState == UIDocumentStateClosed) {
        [appDelegate.arlearnDatabase openWithCompletionHandler:^(BOOL succes) {
            [self fetchARLearnGamesAndRuns: appDelegate.arlearnDatabase];
            [self setupFetchedResultsController];
        }];
    } else if (appDelegate.arlearnDatabase.documentState == UIDocumentStateNormal) {
        [self fetchARLearnGamesAndRuns: appDelegate.arlearnDatabase];
        [self setupFetchedResultsController];
    }
}

- (void) fetchARLearnGamesAndRuns: (UIManagedDocument *)document {
    
    NSString *authToken = [[NSUserDefaults standardUserDefaults] objectForKey:@"auth"];
    if (authToken) {
        [ARLCloudSynchronizer syncronizeRuns:document.managedObjectContext];
        [ARLCloudSynchronizer syncronizeGames:document.managedObjectContext];
    }
}

- (void) setupFetchedResultsController {
    ARLAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
    
    NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"Run"];
    
    request.predicate = [NSPredicate predicateWithFormat: @"deleted = %d", NO];
    
    request.sortDescriptors =[NSArray arrayWithObject:[NSSortDescriptor sortDescriptorWithKey:@"title" ascending:YES selector:@selector(localizedCaseInsensitiveCompare:)]];
    if (appDelegate.arlearnDatabase.documentState == UIDocumentStateNormal) {
        
        self.fetchedResultsController = [[NSFetchedResultsController alloc] initWithFetchRequest:request
                                                                            managedObjectContext:appDelegate.arlearnDatabase.managedObjectContext
                                                                              sectionNameKeyPath:nil cacheName:nil];
    }
}

- (void) viewDidLoad {
    [super viewDidLoad];
    [self useDocument];
    NSLog(@"run view appears viewDidLoad");
}

- (void) viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    ARLAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
    
    if (appDelegate.arlearnDatabase.documentState == UIDocumentStateNormal) {
        [self fetchARLearnGamesAndRuns: appDelegate.arlearnDatabase];
    }
    //    [self setupFetchedResultsController];
}

//- (void) viewWillAppear:(BOOL)animated {
//    [super viewWillAppear:animated];
//    NSLog(@"run view appears viewWillAppear");
//}


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"runCellIdentifier";
    ARLRunViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[ARLRunViewCell alloc]
                initWithStyle:UITableViewCellStyleDefault
                reuseIdentifier:CellIdentifier];
    }
    [self setRunCellValues:[self.fetchedResultsController objectAtIndexPath:indexPath] withCell:cell];
    return cell;
}

-(void) configureCell: (ARLRunViewCell *) cell atIndexPath:(NSIndexPath *)indexPath {
    [self setRunCellValues:[self.fetchedResultsController objectAtIndexPath:indexPath] withCell:cell];
}

- (void) setRunCellValues: (Run *) run withCell:(ARLRunViewCell *) cell {
    cell.runTitleLabel.text = [NSString stringWithFormat:@"%@",run.title];
    cell.runDetail.text = [NSString stringWithFormat:@"owner run: %@", run.owner];
    if ([run.game.hasMap boolValue]) {
        [cell.icon setImage:[UIImage imageNamed: @"icon_maps.png"]];
    } else {
        [cell.icon setImage:[UIImage imageNamed: @"list_icon.png"]];
    }
}


- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if ([segue.identifier isEqualToString:@"runDetailSegue"]) {
        Run * selectedRun = [self.fetchedResultsController objectAtIndexPath:[self.tableView indexPathForSelectedRow]];
//        [ARLCloudSynchronizer synchronizeGeneralItemsWithGame:selectedRun.game ];
//        [ARLCloudSynchronizer synchronizeGeneralItemVisiblityStatementsWithRun:selectedRun ];
        [ARLCloudSynchronizer synchronizeGeneralItemsAndVisibilityStatments:selectedRun];
        for (UIViewController *v in [segue.destinationViewController viewControllers]) {
            if ([v respondsToSelector:@selector(setRun:)]) {
                [v performSelector:@selector(setRun:) withObject:selectedRun];
            }
        }
    }
    
}


@end
