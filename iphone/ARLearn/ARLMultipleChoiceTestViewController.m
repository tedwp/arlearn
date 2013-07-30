//
//  ARLMultipleChoiceTestViewController.m
//  ARLearn
//
//  Created by Stefaan Ternier on 1/17/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLMultipleChoiceTestViewController.h"

@interface ARLMultipleChoiceTestViewController ()

@end

@implementation ARLMultipleChoiceTestViewController

@synthesize generalItem = _generalItem;
@synthesize run = _run;
@synthesize headerText;
@synthesize answerView = _answerView;
@synthesize jsonDict = _jsonDict;

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
    self.headerText.title = self.generalItem.name;

    self.jsonDict = [NSKeyedUnarchiver unarchiveObjectWithData:self.generalItem.json];
    NSLog(@"type is %@", [self.jsonDict objectForKey:@"type"]);
    [self createSubmitButton];
    [self createWebView];
    BOOL isSingleChoice = [@"org.celstec.arlearn2.beans.generalItem.SingleChoiceTest" isEqualToString:[self.jsonDict objectForKey:@"type"]];
    NSLog(@"type is single %d", isSingleChoice);
    [self createAnswerView:isSingleChoice];
    [self setConstraints];

}

- (void) createWebView {
    self.webView = [[UIWebView alloc] init];
    self.webView.translatesAutoresizingMaskIntoConstraints = NO;

    [self.webView loadHTMLString:self.generalItem.richText baseURL:nil];
    [self.view addSubview:self.webView];
}

- (void) createAnswerView:(BOOL) isSingleChoice {
    self.answerView = [[ARLMultipleChoiceAnswerView alloc] initWith:self.jsonDict singleChoice:isSingleChoice];
    self.webView.translatesAutoresizingMaskIntoConstraints = NO;

    [self.view addSubview:self.answerView];
}

- (void) createSubmitButton {
    self.submitButton = [UIButton buttonWithType:UIButtonTypeRoundedRect];
    [self.submitButton setTitle:@"Submit" forState:UIControlStateNormal];

    self.submitButton.translatesAutoresizingMaskIntoConstraints = NO;
    [self.submitButton addTarget:self action:@selector(submitQuestion) forControlEvents:UIControlEventTouchUpInside];

    [self.view addSubview:self.submitButton];
}

- (void) setConstraints {
    

//        UIWebView* webView = self.webView;
    NSDictionary *viewsDictionary =
    [[NSDictionary alloc] initWithObjectsAndKeys:
     self.submitButton, @"submitButton",
     self.answerView, @"answerView",
     self.webView, @"webView",
      nil];

    NSString* vConstraints = [NSString stringWithFormat:@"V:|-[webView]-[answerView(==%d)]-[submitButton]-|", [self.answerView height]];
    NSLog(@"new constraint %@", vConstraints);
    
    [self.view addConstraints:[NSLayoutConstraint
                               constraintsWithVisualFormat:vConstraints
                               options:NSLayoutFormatDirectionLeadingToTrailing
                               metrics:nil
                               views:viewsDictionary]];


    [self.view addConstraints:[NSLayoutConstraint
                                   constraintsWithVisualFormat:@"H:|[webView]|"
                                   options:NSLayoutFormatDirectionLeadingToTrailing
                                   metrics:nil
                                   views:viewsDictionary]];
    
    [self.view addConstraints:[NSLayoutConstraint
                               constraintsWithVisualFormat:@"H:|[answerView]|"
                               options:NSLayoutFormatDirectionLeadingToTrailing
                               metrics:nil
                               views:viewsDictionary]];
    [self.view addConstraints:[NSLayoutConstraint
                               constraintsWithVisualFormat:@"H:|-[submitButton]-|"
                               options:NSLayoutFormatDirectionLeadingToTrailing
                               metrics:nil
                               views:viewsDictionary]];
}



- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

- (void) submitQuestion {
    
    for (NSString * answerId in [self.answerView selectedIds]) {
        
        
        for (NSDictionary * answerDict in [self.jsonDict objectForKey:@"answers"]) {
            if ([answerId isEqualToString:[answerDict objectForKey:@"id"] ]) {
                NSDictionary *myDictionary= [[NSDictionary alloc] initWithObjectsAndKeys:
                                             [answerDict objectForKey:@"answer"], @"answer",
                                             [answerDict objectForKey:@"id"], @"id",
                                             [NSNumber numberWithBool:[(NSNumber*)[answerDict objectForKey:@"isCorrect"] boolValue]], @"isCorrect", nil];
                NSError *error;
                NSData *jsonData = [NSJSONSerialization dataWithJSONObject:myDictionary
                                                                   options:0
                                                                     error:&error];
                
                if (!jsonData) {
                    NSLog(@"JSON error: %@", error);
                } else {
                    
                    NSString *JSONString = [[NSString alloc] initWithBytes:[jsonData bytes] length:[jsonData length] encoding:NSUTF8StringEncoding];
                    NSLog(@"JSON ouput: %@", JSONString);
                    
                    [Response initResponse:self.run forGeneralItem:self.generalItem withValue:JSONString inManagedObjectContext: self.generalItem.managedObjectContext];
                    
                    NSString* action = [NSString stringWithFormat:@"answer_%@", answerId];
                    [Action initAction:action forRun:self.run forGeneralItem:self.generalItem inManagedObjectContext:self.generalItem.managedObjectContext];
                    ARLCloudSynchronizer* synchronizer = [[ARLCloudSynchronizer alloc] init];
                    ARLAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
                    [synchronizer createContext:appDelegate.managedObjectContext];
                    synchronizer.syncResponses = true;
                    synchronizer.syncActions = true;
                    [synchronizer sync];
                }
            }
        }
    }
    
}
@end
