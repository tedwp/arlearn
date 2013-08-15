//
//  ARLFeaturedGamesViewController.m
//  ARLearn
//
//  Created by Stefaan Ternier on 8/14/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLFeaturedGamesViewController.h"

@interface ARLFeaturedGamesViewController ()

@end

@implementation ARLFeaturedGamesViewController

@synthesize searchArray;

- (void)viewDidLoad
{
    [super viewDidLoad];
    NSDictionary *result = [ARLNetwork featured ];
    
    self.searchArray = [NSMutableArray arrayWithArray:[result objectForKey:@"games"]];
    [[self tableView] reloadData];
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [self.searchArray count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"featuredGameCell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier forIndexPath:indexPath];
    
    cell.textLabel.text = [[searchArray objectAtIndex:[indexPath row]] objectForKey:@"title"];
    return cell;
}


- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    NSIndexPath *indexPath = [self.tableView indexPathForSelectedRow];
    NSDictionary* gameDict = [searchArray objectAtIndex:[indexPath row]];
    ARLAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
    Game* game = [Game gameWithDictionary:gameDict inManagedObjectContext:appDelegate.managedObjectContext];
    if ([segue.destinationViewController respondsToSelector:@selector(setGame:)]) {
        [segue.destinationViewController performSelector:@selector(setGame:) withObject:game];
    }
}



@end
