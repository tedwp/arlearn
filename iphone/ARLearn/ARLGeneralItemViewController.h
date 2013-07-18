//
//  ARLGeneralItemViewController.h
//  ARLearn
//
//  Created by Stefaan Ternier on 1/15/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "GeneralItem.h"


@interface ARLGeneralItemViewController : UIViewController

@property (strong, nonatomic) GeneralItem * generalItem;
@property (weak, nonatomic) IBOutlet UINavigationItem *headerText;
@property (weak, nonatomic) IBOutlet UIWebView *webView;
@property (weak, nonatomic) IBOutlet UIButton *provideActionButton;

@end
