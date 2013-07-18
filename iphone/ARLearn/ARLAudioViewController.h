//
//  ARLAudioViewController.h
//  ARLearn
//
//  Created by Stefaan Ternier on 2/7/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <AVFoundation/AVAudioPlayer.h>
#import "GeneralItem.h"
#import "GeneralItemData.h"
#import "ARLDataCollectionWidget.h"
#import "ARLAudioObjectPlayButtons.h"
#import "ARLAudioObjectPlayer.h"

@interface ARLAudioViewController : UIViewController

//@property (strong, nonatomic ) AVAudioPlayer * player;
@property (strong, nonatomic ) ARLAudioObjectPlayer * player;
@property (strong, nonatomic) GeneralItem * generalItem;
@property (strong, nonatomic) Run * run;

@property (strong, nonatomic)  ARLDataCollectionWidget* dataCollectionWidget;

@property (strong, nonatomic) IBOutlet UIView *mainView;
@property (strong, nonatomic)  UIWebView *webView;
@property (weak, nonatomic) IBOutlet UINavigationItem *headerText;



@end
