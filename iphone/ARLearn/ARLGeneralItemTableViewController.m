//
//  ARLGeneralItemTableViewController.m
//  ARLearn
//
//  Created by Stefaan Ternier on 1/14/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLGeneralItemTableViewController.h"
#import "ARLGeneralItemTableViewCell.h"

@interface ARLGeneralItemTableViewController ()


@end

@implementation ARLGeneralItemTableViewController

@synthesize run = _run;

- (void)setupFetchedResultsController {

    NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"CurrentItemVisibility"];
    request.sortDescriptors = [NSArray arrayWithObject:[NSSortDescriptor sortDescriptorWithKey:@"name"
                                                                                     ascending:YES
                                                                                      selector:@selector(localizedCaseInsensitiveCompare:)]];
    request.predicate = [NSPredicate predicateWithFormat:
                         @"visible = 1 and run.runId = %lld",
                         [self.run.runId longLongValue]];
    
    NSSortDescriptor* sortkey = [[NSSortDescriptor alloc] initWithKey:@"item.sortKey" ascending:YES];
    NSArray *sortDescriptors = [[NSArray alloc] initWithObjects:sortkey, nil];
    [request setSortDescriptors:sortDescriptors];
    self.fetchedResultsController = [[NSFetchedResultsController alloc] initWithFetchRequest:request
                                                                        managedObjectContext:self.run.managedObjectContext
                                                                          sectionNameKeyPath:nil
                                                                                   cacheName:nil];

}

- (void) setRun: (Run *) run {
    _run = run;
//    self.title = run.title;
    [self setupFetchedResultsController];
}

- (void) viewDidLoad {
// [[self tabBarItem] setFinishedSelectedImage:[UIImage imageNamed:@"list_icon.png"] withFinishedUnselectedImage:[UIImage imageNamed:@"list_icon.png"]];
//    [[[self tabBarController] tabBar] setBackgroundImage:[UIImage imageNamed:@"list_icon.png"]];
}

- (void) viewDidAppear:(BOOL)animated {
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, 5 * NSEC_PER_SEC), dispatch_get_main_queue(), ^{
        ARLCloudSynchronizer* synchronizer = [[ARLCloudSynchronizer alloc] init];
        ARLAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
        [synchronizer createContext:appDelegate.managedObjectContext];
        synchronizer.gameId = self.run.gameId;
        synchronizer.visibilityRunId = self.run.runId;
        [synchronizer sync];
    });
   
}

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    return self;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 1;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{

    
    GeneralItem * generalItem = ((CurrentItemVisibility*)[self.fetchedResultsController objectAtIndexPath:indexPath]).item;
    ARLGeneralItemTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:generalItem.type];
    if (cell == nil) {
        cell = [[ARLGeneralItemTableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:generalItem.type];
    }
    cell.giTitleLabel.text = generalItem.name;
    cell.giTitleLabel.font = [UIFont boldSystemFontOfSize:16.0f];
    for (Action * action in generalItem.actions) {
        if (action.run == self.run) {
            if ([action.action isEqualToString:@"read"]) {
                cell.giTitleLabel.font = [UIFont systemFontOfSize:16.0f];
            }
        }
    }
//    cell.detailTextLabel.text = [NSString stringWithFormat:@"vis statements %d", [generalItem.visibility count] ];
   
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    ARLGeneralItemTableViewCell *cell = (ARLGeneralItemTableViewCell*) [tableView cellForRowAtIndexPath:indexPath];
    cell.giTitleLabel.font = [UIFont systemFontOfSize:16.0f];
}

-(void) configureCell: (ARLGeneralItemTableViewCell *) cell atIndexPath:(NSIndexPath *)indexPath {
    GeneralItem * generalItem = ((CurrentItemVisibility*)[self.fetchedResultsController objectAtIndexPath:indexPath]).item;

    cell.giTitleLabel.text = generalItem.name;
//    cell.detailTextLabel.text = [NSString stringWithFormat:@"vis statements %d", [generalItem.visibility count] ];
    NSData* icon = [generalItem customIconData];
    if (icon) {
        UIImage * image = [UIImage imageWithData:icon];
        cell.icon.image = image;
    }
}


- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    NSIndexPath *indexPath = [self.tableView indexPathForSelectedRow];
    
    GeneralItem * generalItem = ((CurrentItemVisibility*)[self.fetchedResultsController objectAtIndexPath:indexPath]).item;
    if ([segue.destinationViewController respondsToSelector:@selector(setGeneralItem:)]) {
        [segue.destinationViewController performSelector:@selector(setGeneralItem:) withObject:generalItem];
    }
    if ([segue.destinationViewController respondsToSelector:@selector(setRun:)]) {
        [segue.destinationViewController performSelector:@selector(setRun:) withObject:self.run];
    }
    [Action initAction:@"read" forRun:self.run forGeneralItem:generalItem inManagedObjectContext:generalItem.managedObjectContext];
    [ARLCloudSynchronizer syncActions:generalItem.managedObjectContext];

//    [ARLNetwork publishAction:self.run.runId action:@"read" itemId:generalItem.id itemType:generalItem.type];
}

@end
