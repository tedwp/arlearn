//
//  ARLAudioViewController.m
//  ARLearn
//
//  Created by Stefaan Ternier on 2/7/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLAudioViewController.h"

@interface ARLAudioViewController ()

@end

@implementation ARLAudioViewController

@synthesize generalItem = _generalItem;
@synthesize player;
@synthesize headerText;
@synthesize imagePickerController;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.headerText.title = self.generalItem.name;
    [self.webView loadHTMLString:self.generalItem.richText baseURL:nil];
    NSDictionary * jsonDict = [NSKeyedUnarchiver unarchiveObjectWithData:self.generalItem.json];
    
    NSLog(@"url %@", [jsonDict objectForKey:@"audioFeed"]);
    NSData *fileUrl=[[NSData alloc]initWithContentsOfURL:[NSURL URLWithString:[jsonDict objectForKey:@"audioFeed"]]];
    NSSet *dataSet = self.generalItem.data;
    for(GeneralItemData * data in dataSet) {
        NSLog(@"nsdata %@", data.name);
        fileUrl = data.data;
    }
	NSError *error;

    self.player =     [[AVAudioPlayer alloc] initWithData:fileUrl error:&error];

    if (error) {
        NSLog(@"error: %@", [error description]);
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)playAction:(id)sender {
	self.player.numberOfLoops = 1;
	
	if (self.player == nil)
		NSLog(@"error");
	else
		[self.player play];
}

- (IBAction)enterValue:(id)sender {
    if (!imagePickerController) {
        imagePickerController = [[UIImagePickerController alloc] init];
        if ([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeCamera]) {
            [imagePickerController setSourceType:UIImagePickerControllerSourceTypeCamera];
        }else
        {
            [imagePickerController setSourceType:UIImagePickerControllerSourceTypePhotoLibrary];
        }
        
        // image picker needs a delegate so we can respond to its messages
        [imagePickerController setDelegate:self];
    }
    // Place image picker on the screen
    [self presentModalViewController:imagePickerController animated:YES];
}

- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info
{
    UIImage *image = [info objectForKey:UIImagePickerControllerOriginalImage];
    
//    image = [ImageHelpers imageWithImage:image scaledToSize:CGSizeMake(480, 640)];
    
   // [imageView setImage:image];
    
    [self dismissModalViewControllerAnimated:YES];
}

@end
