//
//  ARLGeneralItemTableViewController.m
//  ARLearn
//
//  Created by Stefaan Ternier on 1/14/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLGeneralItemTableViewController.h"
#import "ARLGeneralItemViewCell.h"
@interface ARLGeneralItemTableViewController ()


@end

@implementation ARLGeneralItemTableViewController

@synthesize run = _run;

- (void)setupFetchedResultsController {
    NSNumber * currentTimeMillis = [NSNumber numberWithFloat:([[NSDate date] timeIntervalSince1970] * 1000 )];
//    NSLog(@"current time %lld", [currentTimeMillis longLongValue]);
    NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"GeneralItem"];
    request.sortDescriptors = [NSArray arrayWithObject:[NSSortDescriptor sortDescriptorWithKey:@"name"
                                                                                     ascending:YES
                                                                                      selector:@selector(localizedCaseInsensitiveCompare:)]];
//    request.predicate = [NSPredicate predicateWithFormat:
//                         @"ownerGame.gameId = %lld ",
//                         [self.run.game.gameId longLongValue]];

    request.predicate = [NSPredicate predicateWithFormat:
                         @"ownerGame.gameId = %lld and SUBQUERY(visibility, $x, $x.runId = %lld and $x.status = 1 and $x.timeStamp < %lld).@count > 0 and SUBQUERY(visibility, $x, $x.runId = %lld and $x.status = 2 and $x.timeStamp < %lld).@count = 0",
                         [self.run.game.gameId longLongValue], [self.run.runId longLongValue], [currentTimeMillis longLongValue], [self.run.runId longLongValue], [currentTimeMillis longLongValue]];

    
    self.fetchedResultsController = [[NSFetchedResultsController alloc] initWithFetchRequest:request
                                                                        managedObjectContext:self.run.managedObjectContext
                                                                          sectionNameKeyPath:nil
                                                                                   cacheName:nil];
}

- (void) setRun: (Run *) run {
    _run = run;
    self.title = run.title;
    [self setupFetchedResultsController];
}

//- (id) init {
//    self = [super init];
//    if (self) {
//        [[self tabBarItem] setFinishedSelectedImage:[UIImage imageNamed:@"list_icon.png"] withFinishedUnselectedImage:[UIImage imageNamed:@"list_icon.png"]];
//    }
//    return self;
//}

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
    ARLGeneralItemViewCell *cell = [tableView dequeueReusableCellWithIdentifier:generalItem.type];
    if (cell == nil) {
        cell = [[ARLGeneralItemViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:generalItem.type];
    }
    cell.itemTitle.text = generalItem.name;
    cell.detailTextLabel.text = [NSString stringWithFormat:@"vis statements %d", [generalItem.visibility count] ];
   
    return cell;
}

-(void) configureCell: (ARLGeneralItemViewCell *) cell atIndexPath:(NSIndexPath *)indexPath {
    GeneralItem * generalItem = [self.fetchedResultsController objectAtIndexPath:indexPath];
    cell.itemTitle.text = generalItem.name;
    cell.detailTextLabel.text = [NSString stringWithFormat:@"vis statements %d", [generalItem.visibility count] ];
}


- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    NSIndexPath *indexPath = [self.tableView indexPathForSelectedRow];
    GeneralItem * generalItem = [self.fetchedResultsController objectAtIndexPath:indexPath];
    if ([segue.destinationViewController respondsToSelector:@selector(setGeneralItem:)]) {
        [segue.destinationViewController performSelector:@selector(setGeneralItem:) withObject:generalItem];
    }
    [ARLNetwork publishAction:self.run.runId action:@"read" itemId:generalItem.id itemType:generalItem.type];
}

@end
