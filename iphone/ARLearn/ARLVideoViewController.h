//
//  ARLVideoViewController.h
//  ARLearn
//
//  Created by Stefaan Ternier on 7/5/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "GeneralItem.h"
#import "GeneralItemData.h"
#import "Run.h"
#import "ARLDataCollectionWidget.h"
#import <MediaPlayer/MediaPlayer.h>

@interface ARLVideoViewController : UIViewController
@property (strong, nonatomic) GeneralItem * generalItem;
@property (strong, nonatomic) Run * run;

@property (weak, nonatomic) IBOutlet UINavigationItem *headerText;

@property (strong, nonatomic)  UIWebView *webView;
@property (strong, nonatomic) UIButton * playButton;
@property (strong, nonatomic)  ARLDataCollectionWidget* dataCollectionWidget;
@property (strong, nonatomic)  NSURL* tempVideoUrl;
@property (strong, nonatomic)  MPMoviePlayerController* moviePlayerController;



@end
