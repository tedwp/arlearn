//
//  ARLInfoViewController.m
//  ARLearn
//
//  Created by Stefaan Ternier on 7/10/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLInfoViewController.h"

@interface ARLInfoViewController ()

@end

@implementation ARLInfoViewController

@synthesize imageArray; //.m

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
    NSArray *colors = [NSArray arrayWithObjects:[UIColor redColor], [UIColor greenColor], [UIColor blueColor], nil];
    for (int i = 0; i < colors.count; i++) {
        CGRect frame;
        frame.origin.x = self.scrollView.frame.size.width * i;
        frame.origin.y = 0;
        frame.size = self.scrollView.frame.size;
        
        UIView *subview = [[UIView alloc] initWithFrame:frame];
        subview.backgroundColor = [colors objectAtIndex:i];
        [self.scrollView addSubview:subview];
    }
    
    if (self.scrollView) {
        NSLog(@"not nil");
    } else {
                NSLog(@"nil");
    }
//    self.scrollView.contentSize = CGSizeMake(self.scrollView.frame.size.width * colors.count, self.scrollView.frame.size.height);
    
//    imageArray = [[NSArray alloc] initWithObjects:@"facebook.png", @"google.png", @"facebook.png", nil];
//    
//    for (int i = 0; i < [imageArray count]; i++) {
//        //We'll create an imageView object in every 'page' of our scrollView.
//        CGRect frame;
//        frame.origin.x = self.scrollView.frame.size.width * i;
//        frame.origin.y = 0;
//        frame.size = self.scrollView.frame.size;
//        
//        UIImageView *imageView = [[UIImageView alloc] initWithFrame:frame];
//        imageView.image = [UIImage imageNamed:[imageArray objectAtIndex:i]];
//        [self.scrollView addSubview:imageView];
//    }
//    //Set the content size of our scrollview according to the total width of our imageView objects.
//    self.scrollView.contentSize = CGSizeMake(self.scrollView.frame.size.width * [self.imageArray count], self.scrollView.frame.size.height);

   
    
}

- (void)scrollViewDidScroll:(UIScrollView *)sender {
    NSLog(@"did scroll");

    // Update the page when more than 50% of the previous/next page is visible
    CGFloat pageWidth = self.scrollView.frame.size.width;
    int page = floor((self.scrollView.contentOffset.x - pageWidth / 2) / pageWidth) + 1;
//    self.pageControl.currentPage = page;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)clcik:(id)sender {
    NSLog(@"clci");
}

- (void)viewDidUnload {
    
    self.scrollView = nil;
}
@end
