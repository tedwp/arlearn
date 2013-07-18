//
//  ARLFileTableViewController.m
//  ARLearn
//
//  Created by Stefaan Ternier on 4/9/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLFileTableViewController.h"

@interface ARLFileTableViewController ()

@end

@implementation ARLFileTableViewController

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
    NSFileManager * fileMrg = [NSFileManager defaultManager];
    NSArray * paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString * documentsDirectory = [paths objectAtIndex:0];
    NSLog(@"doc dir %@", documentsDirectory);
    
    NSString * newString = [NSString stringWithFormat:@"%@/games", documentsDirectory ];
    [DownloadHelper sharedInstance].filePath = newString;
//    NSLog(@"doc dir %@", newString);
//    NSLog(@"succeeded %d", [fileMrg createDirectoryAtPath:newString withIntermediateDirectories:YES attributes:nil error:nil]);
    NSArray * array = [fileMrg contentsOfDirectoryAtPath:[paths objectAtIndex:0] error:nil];
    NSLog(@"gettting rid of warning %@",array.description);
//    int i;
//    for (i = 0; i < [array count]; i++) {
//
//        NSLog(@"sub path dir %@", [array objectAtIndex:0]);
//    }
//    
    NSFileManager * fileManager = [NSFileManager new];
    NSArray * subpaths = [fileManager subpathsOfDirectoryAtPath:documentsDirectory error:nil];
    int i;
    for (i = 0; i < [subpaths count]; i++) {
        
        NSLog(@"sub path dir %@", [subpaths objectAtIndex:i]);
    }
    
    
//    NSLog(@"sub path dir %@", [array objectAtIndex:0]);
//    NSLog(@"succeeded %d", [fileMrg createDirectoryAtPath:documentsDirectory withIntermediateDirectories:YES attributes:nil error:nil]);
    
    
        NSString * url = @"http://ar-learn.appspot.com:80/uploadService/148018/arlearn1/image-1471938190.jpg";
    NSString* audioFilePath = @"/Users/str/Library/Application Support/iPhone Simulator/6.1/Applications/0A7E08E9-9847-4160-873F-29FC35918A39/Documents/games/DO-reception.m4a";
    NSLog(@"file exists at path %d", [fileMrg fileExistsAtPath:audioFilePath]);
    if ([[NSFileManager defaultManager] fileExistsAtPath:audioFilePath])
    {
        NSLog(@"file exists ");
        NSDictionary *fileAttributes = [[NSFileManager defaultManager] attributesOfItemAtPath:audioFilePath error:nil];
        
        NSNumber *fileSizeNumber = [fileAttributes objectForKey:NSFileSize];
        NSLog(@"file size %lld ", [fileSizeNumber longLongValue]);
        
    }
    [fileMrg removeItemAtPath:audioFilePath error:nil];
    [DownloadHelper sharedInstance];
    [DownloadHelper sharedInstance].delegate = self ;
    [DownloadHelper download:url];
//
    
}

- (void) didReceiveData: (NSData *) theData {
    NSLog(@"didReceiveData :");
}
- (void) didReceiveFilename: (NSString *) aName {
    NSLog(@"didReceiveFilename : %@",aName);
    NSLog(@"filePath : %@",[DownloadHelper sharedInstance].filePath);
    
}
- (void) dataDownloadFailed: (NSString *) reason{
    NSLog(@"dataDownloadFailed : %@",reason);
}
- (void) dataDownloadAtPercent: (NSNumber *) aPercent {
    NSLog(@"dataDownloadAtPercent : %@",[aPercent description]);

}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 0;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 0;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier forIndexPath:indexPath];
    
    // Configure the cell...
    
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

@end
