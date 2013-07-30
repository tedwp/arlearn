//
//  ARLNarratorItemViewController.m
//  ARLearn
//
//  Created by Stefaan Ternier on 7/18/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLNarratorItemViewController.h"

@interface ARLNarratorItemViewController ()

@end

@implementation ARLNarratorItemViewController

@synthesize generalItem = _generalItem;
@synthesize run = _run;
@synthesize headerText= _headerText;
@synthesize dataCollectionWidget = _dataCollectionWidget;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
    }
    return self;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

- (void) viewWillDisappear:(BOOL)animated {
    NSLog(@"I will disappear");
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}


- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.headerText.title = self.generalItem.name;
    self.webView = [[UIWebView alloc] init];
    [self.webView loadHTMLString:self.generalItem.richText baseURL:nil];
    NSDictionary * jsonDict = [NSKeyedUnarchiver unarchiveObjectWithData:self.generalItem.json];

    self.dataCollectionWidget = [[ARLDataCollectionWidget alloc] init:[jsonDict objectForKey:@"openQuestion"] viewController:self];
    if (self.dataCollectionWidget.isVisible) {
        self.dataCollectionWidget.run = self.run;
        self.dataCollectionWidget.generalItem = self.generalItem;
    }
    
    [self setConstraints];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(handleDataModelChange:) name:NSManagedObjectContextObjectsDidChangeNotification object:self.generalItem.managedObjectContext];

}

- (void)handleDataModelChange:(NSNotification *)note
{
    NSSet *updatedObjects = [[note userInfo] objectForKey:NSUpdatedObjectsKey];
    NSSet *deletedObjects = [[note userInfo] objectForKey:NSDeletedObjectsKey];
    
    for(NSManagedObject *obj in updatedObjects){
        
        if ([[obj entity].name isEqualToString:@"GeneralItem"]) {
            GeneralItem* changedObject = (GeneralItem*) obj;
            if (self.generalItem == changedObject) {
                self.headerText.title = self.generalItem.name;
                
                [self.webView loadHTMLString:self.generalItem.richText baseURL:nil];
            }
        }
        
    }

    for(NSManagedObject *obj in deletedObjects){
        if ([[obj entity].name isEqualToString:@"GeneralItem"]) {
            GeneralItem* changedObject = (GeneralItem*) obj;
            if (self.generalItem == changedObject) {
                NSLog(@"little less easy... I was deleted");

                [self.navigationController popViewControllerAnimated:NO];
                [self dismissViewControllerAnimated:TRUE completion:nil];

            }
        }
    }
    
}

- (void) setConstraints {
    
    UIWebView* webView = self.webView;
    webView.translatesAutoresizingMaskIntoConstraints = NO;
    
    ARLDataCollectionWidget* widget = self.dataCollectionWidget;
    [self.mainView addSubview:webView];
    [self.mainView addSubview:self.dataCollectionWidget];
    
    NSDictionary *viewsDictionary;
    if (widget) {
        viewsDictionary =
        [[NSDictionary alloc] initWithObjectsAndKeys:
         webView, @"webView",
         widget, @"widget", nil];
    } else {
        viewsDictionary =
        [[NSDictionary alloc] initWithObjectsAndKeys:
         webView, @"webView", nil];
    }
    
    NSString* verticalContstraint;
    if (widget.isVisible) {
        verticalContstraint = @"V:|[webView(>=100)]-[widget(==80)]|";
        
    } else {
        verticalContstraint = @"V:|[webView(>=100)]|";
    }
    
    [self.mainView addConstraints:[NSLayoutConstraint
                                   constraintsWithVisualFormat:verticalContstraint
                                   options:NSLayoutFormatDirectionLeadingToTrailing
                                   metrics:nil
                                   views:viewsDictionary]];
  
    [self.mainView addConstraints:[NSLayoutConstraint
                                   constraintsWithVisualFormat:@"H:|[webView]|"
                                   options:NSLayoutFormatDirectionLeadingToTrailing
                                   metrics:nil
                                   views:NSDictionaryOfVariableBindings(webView)]];
    
    if (widget.isVisible) {
        [self.mainView addConstraints:[NSLayoutConstraint
                                       constraintsWithVisualFormat:@"H:|[widget]|"
                                       options:NSLayoutFormatDirectionLeadingToTrailing
                                       metrics:nil
                                       views:NSDictionaryOfVariableBindings(widget)]];
    }
    
}


@end
