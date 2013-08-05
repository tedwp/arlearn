//
//  ARLInfoViewController.h
//  ARLearn
//
//  Created by Stefaan Ternier on 7/10/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ARLInfoViewController : UIViewController
@property (weak, nonatomic) IBOutlet UIScrollView *scrollView;
@property (weak, nonatomic) IBOutlet UIPageControl *pageController;
@property (nonatomic, strong) NSArray *imageArray;
@property (strong, nonatomic) IBOutlet UIView *view;
- (IBAction)clcik:(id)sender;
@end
