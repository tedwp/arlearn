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

@interface ARLAudioViewController : UIViewController

@property (strong, nonatomic ) AVAudioPlayer * player;
@property (strong, nonatomic) GeneralItem * generalItem;
@property (strong, nonatomic) UIImagePickerController * imagePickerController;

@property (weak, nonatomic) IBOutlet UIWebView *webView;
@property (weak, nonatomic) IBOutlet UINavigationItem *headerText;
- (IBAction)playAction:(id)sender;
- (IBAction)enterValue:(id)sender;

@end
