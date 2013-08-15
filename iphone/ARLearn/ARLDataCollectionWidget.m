//
//  ARLDataCollectionWidget.m
//  ARLearn
//
//  Created by Stefaan Ternier on 7/11/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLDataCollectionWidget.h"

@implementation ARLDataCollectionWidget

@synthesize withAudio = _withAudio;
@synthesize withPicture = _withPicture;
@synthesize withText = _withText;
@synthesize withValue = _withValue;
@synthesize withVideo = _withVideo;

@synthesize isVisible = _isVisible;

@synthesize generalItem = _generalItem;
@synthesize run = _run;
@synthesize valueTextField = _valueTextField;
@synthesize imagePickerController = _imagePickerController;
@synthesize generalItemViewController = _generalItemViewController;


@synthesize textDescription = _textDescription;
@synthesize valueDescription = _valueDescription;

- (UIButton *)addButtonWithImage:(NSString *)imageString action:(SEL)selector {
    UIButton *button = [UIButton buttonWithType:UIButtonTypeRoundedRect];
    [button addTarget:self action:selector forControlEvents:UIControlEventTouchUpInside];
    button.translatesAutoresizingMaskIntoConstraints = NO;
    UIImage * image = [UIImage imageNamed:imageString];
    
    [button setBackgroundImage:image forState:UIControlStateNormal];
    return button;
}

- (id) init :(NSDictionary *) jsonDict viewController: (UIViewController*) viewController {
    
    if (self = [super init]) {
        _generalItemViewController = viewController;
        if (!jsonDict) {
            self.isVisible = NO;
            return self;
        }
        self.isVisible = YES;
        self.withAudio = [(NSNumber*)[jsonDict objectForKey:@"withAudio"] intValue] ==1;
        self.withPicture = [(NSNumber*)[jsonDict objectForKey:@"withPicture"] intValue] ==1;
        self.withText = [(NSNumber*)[jsonDict objectForKey:@"withText"] intValue] ==1;
        self.withValue = [(NSNumber*)[jsonDict objectForKey:@"withValue"] intValue] ==1;
        self.withVideo = [(NSNumber*)[jsonDict objectForKey:@"withVideo"] intValue] ==1;
        self.textDescription = [jsonDict objectForKey:@"textDescription"];
        self.valueDescription = [jsonDict objectForKey:@"valueDescription"];
        
        self.backgroundColor = [UIColor clearColor];
        self.translatesAutoresizingMaskIntoConstraints = NO;  //This part hung me up
        
        NSString * audioImage;
        if (self.withAudio) {
            audioImage = @"dc_voice_search_128.png";
        } else {
            audioImage = @"dc_voice_search_128_grey.png";
        }
        
        
        
        
        
        UIButton * audioButton = [self addButtonWithImage:audioImage action:@selector(collectAudio) ];
        
        NSString * imageImage;
        if (self.withPicture) {
            imageImage = @"dc_camera_128.png";
        } else {
            imageImage = @"dc_camera_128_grey.png";
        }
        
        UIButton * imageButton = [self addButtonWithImage:imageImage action:@selector(collectImage) ];
        
        NSString * videoImage;
        if (self.withVideo) {
            videoImage = @"dc_video_128.png";
        } else {
            videoImage = @"dc_video_128_grey.png";
        }
        
        UIButton * videoButton = [self addButtonWithImage:videoImage action:@selector(collectVideo)];
        
        NSString * valueImage;
        if (self.withValue) {
            valueImage = @"dc_calculator_128.png";
        } else {
            valueImage = @"dc_calculator_128_grey.png";
        }
        UIButton * noteButton = [self addButtonWithImage:valueImage action:@selector(collectNumber) ];
        
        NSString * textImage;
        if (self.withText) {
            textImage = @"dc_note_128.png";
        } else {
            textImage = @"dc_note_128_grey.png";
        }
        UIButton * textButton = [self addButtonWithImage:textImage action:@selector(collectText) ];
        
        
        [self addSubview:audioButton];
        [self addSubview:imageButton];
        [self addSubview:videoButton];
        [self addSubview:noteButton];
        [self addSubview:textButton];
        
        NSDictionary *viewsDictionary =
        [[NSDictionary alloc] initWithObjectsAndKeys:
         audioButton, @"audioButton",
         imageButton, @"imageButton",
         videoButton, @"videoButton",
         noteButton, @"noteButton",
         textButton, @"textButton", nil];
        
        [self addConstraints:[NSLayoutConstraint
                              constraintsWithVisualFormat:@"V:|-[audioButton(==50)]"
                              options:0
                              metrics:nil
                              views:viewsDictionary]];
        
        [self addConstraints:[NSLayoutConstraint
                              constraintsWithVisualFormat:@"V:|-[imageButton(==50)]"
                              options:0
                              metrics:nil
                              views:viewsDictionary]];
        
        [self addConstraints:[NSLayoutConstraint
                              constraintsWithVisualFormat:@"V:|-[videoButton(==50)]"
                              options:0
                              metrics:nil
                              views:viewsDictionary]];
        
        [self addConstraints:[NSLayoutConstraint
                              constraintsWithVisualFormat:@"V:|-[noteButton(==50)]"
                              options:0
                              metrics:nil
                              views:viewsDictionary]];
        
        [self addConstraints:[NSLayoutConstraint
                              constraintsWithVisualFormat:@"V:|-[textButton(==50)]"
                              options:0
                              metrics:nil
                              views:viewsDictionary]];
        
        [self addConstraints:[NSLayoutConstraint
                              constraintsWithVisualFormat:@"H:[audioButton(==50)]-5-[imageButton(==audioButton)]-5-[videoButton(==audioButton)]-5-[noteButton(==audioButton)]-5-[textButton(==audioButton)]"
                              options:0
                              metrics:nil
                              views:viewsDictionary]];
        
        NSLayoutConstraint *center = [NSLayoutConstraint
                                      constraintWithItem:videoButton attribute:NSLayoutAttributeCenterX
                                      relatedBy:NSLayoutRelationEqual
                                      toItem:self attribute:NSLayoutAttributeCenterX
                                      multiplier:1 constant:0];
        [self addConstraint:center];
    }
    return self;
}

- (void) collectAudio {
    ARLAudioRecorderViewController *controller = [[ARLAudioRecorderViewController alloc] init];
    controller.run = self.run;
    controller.generalItem = self.generalItem;
    [[self.generalItemViewController navigationController] pushViewController:controller animated:TRUE];
    //    [self.generalItemViewController presentViewController:controller animated:TRUE completion:nil];
    
}

- (void) collectNumber{
    
    UIAlertView *myAlertView = [[UIAlertView alloc] initWithTitle:self.valueDescription message:@"this gets covered" delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"OK", nil];
    self.valueTextField = [[UITextField alloc] initWithFrame:CGRectMake(12.0, 45.0, 260.0, 25.0)];
    [self.valueTextField setBackgroundColor:[UIColor whiteColor]];
    [myAlertView addSubview:self.valueTextField];
    [myAlertView show];
    
}

- (void) collectText{
    
    UIAlertView *myAlertView = [[UIAlertView alloc] initWithTitle:self.textDescription message:@"this gets covered" delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"OK", nil];
    self.valueTextField = [[UITextField alloc] initWithFrame:CGRectMake(12.0, 45.0, 260.0, 25.0)];
    [self.valueTextField setBackgroundColor:[UIColor whiteColor]];
    [myAlertView addSubview:self.valueTextField];
    [myAlertView show];
    
}


- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    NSString *title = [alertView buttonTitleAtIndex:buttonIndex];
    if ([title isEqualToString:@"OK"]) {
        
        [Response createTextResponse: self.valueTextField.text withRun:self.run withGeneralItem:self.generalItem ];
        [Action initAction:@"answer_given" forRun:self.run forGeneralItem:self.generalItem inManagedObjectContext:self.generalItem.managedObjectContext];
        
        NSError *error = nil;
        if (self.generalItem.managedObjectContext) {
            if ([self.generalItem.managedObjectContext hasChanges]){
                if (![self.generalItem.managedObjectContext save:&error]) {
                    NSLog(@"Unresolved error %@, %@", error, [error userInfo]);
                    abort();
                }
            }
        }
        [ ARLCloudSynchronizer syncResponses:self.generalItem.managedObjectContext];
    }
    
}

- (void) collectVideo {
    
    if([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeCamera]) {
        
        self.imagePickerController = [[UIImagePickerController alloc] init];
        self.imagePickerController.delegate = self;
        self.imagePickerController.sourceType = UIImagePickerControllerSourceTypeCamera;
        self.imagePickerController.mediaTypes = [[NSArray alloc] initWithObjects: (NSString *) kUTTypeMovie, nil];
        if([UIImagePickerController isCameraDeviceAvailable:UIImagePickerControllerCameraDeviceRear]) {
            self.imagePickerController.cameraDevice = UIImagePickerControllerCameraDeviceRear;
        } else {
            self.imagePickerController.cameraDevice =  UIImagePickerControllerCameraDeviceFront;
        }
        
        //        [self presentModalViewController:self.imagePickerController animated:YES];
        [self.generalItemViewController presentViewController:self.imagePickerController animated:YES completion:nil];
        
    }
    //    if (!self.imagePickerController) {
    //        self.imagePickerController = [[UIImagePickerController alloc] init];
    //        if ([UIImagePickerController isCameraDeviceAvailable:[self.imagePickerController cameraDevice]]) {
    //            [self.imagePickerController takePicture];
    //
    //        }else
    //        {
    //            [self.imagePickerController setSourceType:UIImagePickerControllerSourceTypePhotoLibrary];
    //        }
    //
    //        // image picker needs a delegate so we can respond to its messages
    //        [self.imagePickerController setDelegate:self];
    //    }
    //    // Place image picker on the screen
    //    [self.generalItemViewController presentViewController:self.imagePickerController animated:YES completion:nil];
}

- (void) collectImage{
    
    if (!self.imagePickerController) {
        self.imagePickerController = [[UIImagePickerController alloc] init];
        if ([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeCamera]) {
            [self.imagePickerController setSourceType:UIImagePickerControllerSourceTypeCamera];
        }else
        {
            [self.imagePickerController setSourceType:UIImagePickerControllerSourceTypePhotoLibrary];
        }
        
        // image picker needs a delegate so we can respond to its messages
        [self.imagePickerController setDelegate:self];
    }
    // Place image picker on the screen
    [self.generalItemViewController presentViewController:self.imagePickerController animated:YES completion:nil];
}

- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info {
    UIImage *image = [info objectForKey:UIImagePickerControllerOriginalImage];
    if (image) {
        NSData *imageData = UIImageJPEGRepresentation(image, 0.5);
        [Response createImageResponse:imageData width:[NSNumber numberWithFloat:image.size.width] height:[NSNumber numberWithFloat:image.size.height]  withRun:self.run withGeneralItem:self.generalItem];
    } else {
        id object = [info objectForKey:UIImagePickerControllerMediaURL];
        NSLog(@"dict %@", info);
        NSLog(@"object %@", [object class ]);
        NSData* videoData = [NSData dataWithContentsOfURL:object];
        [Response createVideoResponse:videoData withRun:self.run withGeneralItem:self.generalItem];
        
//        [picker dismissViewControllerAnimated:YES completion:NULL];
    }
    
    [Action initAction:@"answer_given" forRun:self.run forGeneralItem:self.generalItem inManagedObjectContext:self.generalItem.managedObjectContext];
    
    [self.generalItemViewController dismissViewControllerAnimated:YES completion:nil];
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

}

@end
