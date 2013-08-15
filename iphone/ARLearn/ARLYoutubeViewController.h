//
//  ARLYoutubeViewController.h
//  ARLearn
//
//  Created by Stefaan Ternier on 8/6/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "GeneralItem.h"
#import "GeneralItemData.h"
#import "Run.h"

@interface ARLYoutubeViewController : UIViewController

@property (strong, nonatomic) GeneralItem * generalItem;
@property (strong, nonatomic) Run * run;

@property (weak, nonatomic) IBOutlet UINavigationItem *headerText;

@property (strong, nonatomic)  UIWebView *webView;
@property (strong, nonatomic) UIButton * youtubePlay;

@end
