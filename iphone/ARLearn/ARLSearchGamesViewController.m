//
//  ARLSearchGamesViewController.m
//  ARLearn
//
//  Created by Stefaan Ternier on 8/12/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLSearchGamesViewController.h"

@interface ARLSearchGamesViewController ()

@end

@implementation ARLSearchGamesViewController

@synthesize searchBar;
@synthesize searchArray;

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];

    searchBar.delegate = self;
    
    
    
}

- (void) viewDidAppear:(BOOL)animated {
    [self.navigationController setNavigationBarHidden:YES animated:YES];
}

- (void)searchBarSearchButtonClicked:(UISearchBar *)bar {
    NSDictionary *result = [ARLNetwork search:bar.text ];
    NSLog(@"clicked search %@", result);
    NSLog(@"clicked search %@", [[result objectForKey:@"games" ] class]);
    
    self.searchArray = [NSMutableArray arrayWithArray:[result objectForKey:@"games"]];
    [[self tableView] reloadData];
    [bar endEditing:YES];
    
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [self.searchArray count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"gameCell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier forIndexPath:indexPath];
    
    cell.textLabel.text = [[searchArray objectAtIndex:[indexPath row]] objectForKey:@"title"];
    NSLog(@"returning %@", [searchArray objectAtIndex:[indexPath row]]);
    return cell;
}

/*
// Override to support conditional editing of the table view.
- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the specified item to be editable.
    return YES;
}
*/

/*
// Override to support editing the table view.
- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        // Delete the row from the data source
        [tableView deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationFade];
    }   
    else if (editingStyle == UITableViewCellEditingStyleInsert) {
        // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
    }   
}
*/

/*
// Override to support rearranging the table view.
- (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath
{
}
*/

/*
// Override to support conditional rearranging of the table view.
- (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the item to be re-orderable.
    return YES;
}
*/

#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Navigation logic may go here. Create and push another view controller.
    /*
     <#DetailViewController#> *detailViewController = [[<#DetailViewController#> alloc] initWithNibName:@"<#Nib name#>" bundle:nil];
     // ...
     // Pass the selected object to the new view controller.
     [self.navigationController pushViewController:detailViewController animated:YES];
     */
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    NSIndexPath *indexPath = [self.tableView indexPathForSelectedRow];
    NSNumber* gameId = [[searchArray objectAtIndex:[indexPath row]] objectForKey:@"gameId"];
        ARLAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
    Game* game = [Game gameWithDictionary:[ARLNetwork game:gameId] inManagedObjectContext:appDelegate.managedObjectContext];
    if ([segue.destinationViewController respondsToSelector:@selector(setGame:)]) {
        [segue.destinationViewController performSelector:@selector(setGame:) withObject:game];
    }
}

@end
