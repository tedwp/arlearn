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
    NSNumber * currentTimeMillis = [NSNumber numberWithFloat:([[NSDate date] timeIntervalSince1970] * 1000 )];
    NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"GeneralItem"];
    request.sortDescriptors = [NSArray arrayWithObject:[NSSortDescriptor sortDescriptorWithKey:@"name"
                                                                                     ascending:YES
                                                                                      selector:@selector(localizedCaseInsensitiveCompare:)]];
    NSLog(@"ownerGame.gameId = %lld and SUBQUERY(visibility, $x, $x.runId = %lld and $x.status = 1 and $x.timeStamp < %lld).@count > 0 and SUBQUERY(visibility, $x, $x.runId = %lld and $x.status = 2 and $x.timeStamp < %lld).@count = 0",
          [self.run.game.gameId longLongValue], [self.run.runId longLongValue], [currentTimeMillis longLongValue], [self.run.runId longLongValue], [currentTimeMillis longLongValue]);
    request.predicate = [NSPredicate predicateWithFormat:
                         @"ownerGame.gameId = %lld and SUBQUERY(visibility, $x, $x.runId = %lld and $x.status = 1 and $x.timeStamp < %lld).@count > 0 and SUBQUERY(visibility, $x, $x.runId = %lld and $x.status = 2 and $x.timeStamp < %lld).@count = 0",
                         [self.run.game.gameId longLongValue], [self.run.runId longLongValue], [currentTimeMillis longLongValue], [self.run.runId longLongValue], [currentTimeMillis longLongValue]];

    
    NSSortDescriptor* sortkey = [[NSSortDescriptor alloc] initWithKey:@"sortKey" ascending:YES];
    NSArray *sortDescriptors = [[NSArray alloc] initWithObjects:sortkey, nil];
    [request setSortDescriptors:sortDescriptors];
    self.fetchedResultsController = [[NSFetchedResultsController alloc] initWithFetchRequest:request
                                                                        managedObjectContext:self.run.managedObjectContext
                                                                          sectionNameKeyPath:nil
                                                                                   cacheName:nil];
    
//    for (GeneralItem* gi in self.run.game.hasItems){
//        NSLog(@"general item : %@", gi.name);
//        for (GeneralItemVisibility * vis in gi.visibility) {
//                NSLog(@"vis  %@", vis.status);
//        }
//        
//    }
//            NSLog(@"en iteratior");
}

- (void) setRun: (Run *) run {
    _run = run;
    self.title = run.title;
    [self setupFetchedResultsController];
}

- (void) viewDidLoad {
 [[self tabBarItem] setFinishedSelectedImage:[UIImage imageNamed:@"list_icon.png"] withFinishedUnselectedImage:[UIImage imageNamed:@"list_icon.png"]];
//    [[[self tabBarController] tabBar] setBackgroundImage:[UIImage imageNamed:@"list_icon.png"]];
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

    
    GeneralItem * generalItem = [self.fetchedResultsController objectAtIndexPath:indexPath];
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
    GeneralItem * generalItem = [self.fetchedResultsController objectAtIndexPath:indexPath];
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
    
    GeneralItem * generalItem = [self.fetchedResultsController objectAtIndexPath:indexPath];
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
