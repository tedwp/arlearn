//
//  ARLMultipleChoiceTestViewController.h
//  ARLearn
//
//  Created by Stefaan Ternier on 1/17/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "GeneralItem.h"
#import "Run.h"
#import "ARLMultipleChoiceAnswerView.h"
#import "Response+Create.h"
#import "ARLCloudSynchronizer.h"

@interface ARLMultipleChoiceTestViewController : UIViewController 

@property (strong, nonatomic) GeneralItem * generalItem;
@property (strong, nonatomic) Run * run;

@property (weak, nonatomic) IBOutlet UINavigationItem *headerText;
@property (strong, nonatomic)  UIWebView *webView;
@property (strong, nonatomic)  ARLMultipleChoiceAnswerView * answerView;
@property (strong, nonatomic)  UIButton * submitButton;
@property (strong, nonatomic)  NSDictionary* jsonDict;

@end
