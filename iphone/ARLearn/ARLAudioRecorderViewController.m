//
//  ARLAudioRecorderViewController.m
//  ARLearn
//
//  Created by Stefaan Ternier on 8/9/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLAudioRecorderViewController.h"

@interface ARLAudioRecorderViewController ()

@end

@implementation ARLAudioRecorderViewController


- (void) clickedSaveButton: (NSData*) audioData{
    
    [Response createAudioResponse:audioData  withRun:self.run withGeneralItem:self.generalItem];
    [Action initAction:@"answer_given" forRun:self.run forGeneralItem:self.generalItem inManagedObjectContext:self.generalItem.managedObjectContext];
    
        NSError *error = nil;
        if (self.generalItem.managedObjectContext) {
            if ([self.generalItem.managedObjectContext hasChanges]){
                if (![self.generalItem.managedObjectContext save:&error]) {
                    NSLog(@"Unresolved error %@, %@", error, [error userInfo]);
                    abort();
                }
                [ ARLCloudSynchronizer syncResponses: self.generalItem.managedObjectContext];
            }
    
        }
    
    [self.navigationController popViewControllerAnimated:YES];

}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.view.backgroundColor = [UIColor whiteColor];
    
    self.countField = [[UILabel alloc] init];
    self.countField.text = @"00:00";
    [self.countField setFont:[UIFont fontWithName:@"Arial" size:50]];
    self.countField.translatesAutoresizingMaskIntoConstraints = NO;
    [[self view] addSubview:self.countField];
    
    self.saveButton = [[UIButton alloc] init];
    self.saveButton.translatesAutoresizingMaskIntoConstraints = NO;
    self.saveButton.hidden = YES;
    [self.saveButton setBackgroundImage:[UIImage imageNamed:@"save"] forState:UIControlStateNormal];
    [[self view] addSubview:self.saveButton];
    
    
    self.recordButtons = [[ARLAudioRecordButtons alloc] init];
    [[self view] addSubview:self.recordButtons];
    
    
    self.recorder = [[ARLAudioRecorder alloc] init];
    
    [self setConstraints];
    self.recorder.status = ReadyToRecordPlay;
    self.recorder.buttons = self.recordButtons;
    self.recorder.controller = self;
    
    self.recordButtons.recorder = self.recorder;
    [self.recordButtons setButtonsAccordingToStatus];
}

- (void) setConstraints {
    NSDictionary *   viewsDictionary =
    [[NSDictionary alloc] initWithObjectsAndKeys:
     self.countField, @"countField",
     self.saveButton, @"saveButton",
     self.recordButtons, @"recordButtons", nil];
    
    [self.view addConstraints:[NSLayoutConstraint
                               constraintsWithVisualFormat:@"V:[countField]-[saveButton]-[recordButtons(==80)]-|"
                               options:NSLayoutFormatDirectionLeadingToTrailing
                               metrics:nil
                               views:viewsDictionary]];
    
    
    
    [self.view addConstraint:[NSLayoutConstraint
                              constraintWithItem:self.recordButtons attribute:NSLayoutAttributeCenterX
                              relatedBy:NSLayoutRelationEqual
                              toItem:self.view attribute:NSLayoutAttributeCenterX
                              multiplier:1 constant:0]];
    [self.view addConstraint:[NSLayoutConstraint
                              constraintWithItem:self.saveButton attribute:NSLayoutAttributeCenterX
                              relatedBy:NSLayoutRelationEqual
                              toItem:self.view attribute:NSLayoutAttributeCenterX
                              multiplier:1 constant:0]];
    [self.view addConstraint:[NSLayoutConstraint
                              constraintWithItem:self.countField attribute:NSLayoutAttributeCenterX
                              relatedBy:NSLayoutRelationEqual
                              toItem:self.view attribute:NSLayoutAttributeCenterX
                              multiplier:1 constant:0]];
    
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
