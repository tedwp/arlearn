//
//  ARLScanAuthView.h
//  ARLearn
//
//  Created by Stefaan Ternier on 7/25/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <QuartzCore/QuartzCore.h>
#import "ZBarSDK.h"

@interface ARLScanAuthView : UIView

@property (strong, nonatomic) UILabel * scanLabel;
@property (strong, nonatomic) UIButton * scanImage;
@property (weak, nonatomic) UIViewController<ZBarReaderDelegate> * viewController;


@end
