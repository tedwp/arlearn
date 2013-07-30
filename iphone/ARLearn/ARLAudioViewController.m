//
//  ARLAudioViewController.m
//  ARLearn
//
//  Created by Stefaan Ternier on 2/7/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLAudioViewController.h"

@interface ARLAudioViewController ()

@end

@implementation ARLAudioViewController

@synthesize generalItem = _generalItem;
@synthesize run = _run;
@synthesize player;
@synthesize headerText;
@synthesize dataCollectionWidget;

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
    self.webView = [[UIWebView alloc] init];
    [self.webView loadHTMLString:self.generalItem.richText baseURL:nil];
    NSDictionary * jsonDict = [NSKeyedUnarchiver unarchiveObjectWithData:self.generalItem.json];
    
    NSData *mediaData;
    NSSet *dataSet = self.generalItem.data;
    for(GeneralItemData * data in dataSet) {
        mediaData = data.data;
    }
    self.player =[[ARLAudioObjectPlayer alloc] init:mediaData];
    
    self.dataCollectionWidget = [[ARLDataCollectionWidget alloc] init:[jsonDict objectForKey:@"openQuestion"] viewController:self];
    if (self.dataCollectionWidget.isVisible) {
        self.dataCollectionWidget.run = self.run;
        self.dataCollectionWidget.generalItem = self.generalItem;
    }
    
    [self setConstraints];
}

- (void) setConstraints {
    ARLAudioObjectPlayButtons* playButtons = [[ARLAudioObjectPlayButtons alloc] init];
    playButtons.player = self.player;
    self.player.buttons = playButtons;
    
    UIWebView* webView = self.webView;
    webView.translatesAutoresizingMaskIntoConstraints = NO;
    
    ARLDataCollectionWidget* widget = self.dataCollectionWidget;
    [self.mainView addSubview:webView];
    [self.mainView addSubview:playButtons];
    [self.mainView addSubview:self.dataCollectionWidget];
    
    NSDictionary *viewsDictionary;
    if (widget) {
        viewsDictionary =
        [[NSDictionary alloc] initWithObjectsAndKeys:
         webView, @"webView",
         playButtons, @"playButtons",
         widget, @"widget", nil];
    } else {
        viewsDictionary =
        [[NSDictionary alloc] initWithObjectsAndKeys:
         webView, @"webView",
         playButtons, @"playButtons", nil];
    }
    
    NSString* verticalContstraint;
    if (widget.isVisible) {
        NSLog(@"widget vis");
        verticalContstraint = @"V:|-(>=100)-[playButtons(==70)][widget(==80)]|";
        //        verticalContstraint = @"V:|[webView(>=100)]-[playButtons(==70)][widget(==80)]|";
        
    } else {
        NSLog(@"widget nt vis");
        verticalContstraint = @"V:|-(>=100)-[playButtons(==70)]|";
    }
    
    [self.mainView addConstraints:[NSLayoutConstraint
                                   constraintsWithVisualFormat:verticalContstraint
                                   options:NSLayoutFormatDirectionLeadingToTrailing
                                   metrics:nil
                                   views:viewsDictionary]];
    [self.mainView addConstraints:[NSLayoutConstraint
                                   constraintsWithVisualFormat:@"V:|[webView(>=100)]|"
                                   options:NSLayoutFormatDirectionLeadingToTrailing
                                   metrics:nil
                                   views:viewsDictionary]];
    
    [self.mainView addConstraints:[NSLayoutConstraint
                                   constraintsWithVisualFormat:@"H:|[playButtons]|"
                                   options:NSLayoutFormatDirectionLeadingToTrailing
                                   metrics:nil
                                   views:NSDictionaryOfVariableBindings(playButtons)]];
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

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

@end
