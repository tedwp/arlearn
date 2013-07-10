//
//  ARLOauthListViewController.h
//  ARLearn
//
//  Created by Stefaan Ternier on 6/24/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ARLOauthWebViewController.h"
//#import "ZXingWidgetController.h"
//#import "MultiFormatReader.h"
//#import "ARLNetwork.h"
//#import "QRCodeReader.h"
#import "ZBarSDK.h"



@interface ARLOauthListViewController : UIViewController  < ZBarReaderDelegate >//<ZXingDelegate>

@property (nonatomic, assign) BOOL *dismiss;
- (IBAction)facebookButton:(id)sender;

- (IBAction)googleButton:(id)sender;

- (IBAction)qrScan:(id)sender;

- (void)disappear;

@end
