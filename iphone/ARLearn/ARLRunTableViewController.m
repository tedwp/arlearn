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

@synthesize backButton;
-(BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    return (interfaceOrientation == UIInterfaceOrientationPortrait) || (interfaceOrientation == UIInterfaceOrientationPortraitUpsideDown);
    //     return (interfaceOrientation == UIInterfaceOrientationPortrait) ;
}

-(NSUInteger)supportedInterfaceOrientations
{
    return UIInterfaceOrientationMaskPortrait;
}

- (BOOL)shouldAutorotate {
    return NO;
}

- (void) fetchARLearnGamesAndRuns: (NSManagedObjectContext *)managedObjectContext {
    
    NSString *authToken = [[NSUserDefaults standardUserDefaults] objectForKey:@"auth"];
    if (authToken) {
        ARLCloudSynchronizer* synchronizer = [[ARLCloudSynchronizer alloc] init];
        [synchronizer createContext:managedObjectContext];

        synchronizer.syncRuns = YES;
        synchronizer.syncGames = YES;
        [synchronizer sync];
    }
}

- (void) setupFetchedResultsController {
    ARLAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
    
    NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"Run"];
    
    request.predicate = [NSPredicate predicateWithFormat: @"deleted = %d", NO];
    
    request.sortDescriptors =[NSArray arrayWithObject:[NSSortDescriptor sortDescriptorWithKey:@"title" ascending:YES selector:@selector(localizedCaseInsensitiveCompare:)]];
    
    self.fetchedResultsController = [[NSFetchedResultsController alloc] initWithFetchRequest:request
                                                                            managedObjectContext:appDelegate.managedObjectContext
                                                                              sectionNameKeyPath:nil cacheName:nil];
}

- (void) viewDidLoad {
    [super viewDidLoad];
     ARLAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
    [self fetchARLearnGamesAndRuns: appDelegate.managedObjectContext];
    [self setupFetchedResultsController];
    self.backButton.title = NSLocalizedString(@"back", nil);

}



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
    cell.runDetail.text = @"";//[NSString stringWithFormat:@"owner run: %@", run.owner];
    if ([run.game.hasMap boolValue]) {
        [cell.icon setImage:[UIImage imageNamed: @"icon_maps.png"]];
    } else {
        [cell.icon setImage:[UIImage imageNamed: @"list_icon.png"]];
    }
}


- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if ([segue.identifier isEqualToString:@"runDetailSegue"]) {
        Run * selectedRun = [self.fetchedResultsController objectAtIndexPath:[self.tableView indexPathForSelectedRow]];
        [[NSUserDefaults standardUserDefaults] setObject:selectedRun.runId forKey:@"currentRun"];

        ARLCloudSynchronizer* synchronizer = [[ARLCloudSynchronizer alloc] init];
        ARLAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
        [synchronizer createContext:appDelegate.managedObjectContext];
        synchronizer.gameId = selectedRun.gameId;
        synchronizer.visibilityRunId = selectedRun.runId;
        [synchronizer sync];
        for (UIViewController *v in [segue.destinationViewController viewControllers]) {
            if ([v respondsToSelector:@selector(setRun:)]) {
                [v performSelector:@selector(setRun:) withObject:selectedRun];
            }
        }
    }
    
}


@end
