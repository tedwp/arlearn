//
//  ARLDataCollectionWidget.h
//  ARLearn
//
//  Created by Stefaan Ternier on 7/11/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ARLAppDelegate.h"
#import "Response+Create.h"
#import "GeneralItem+ARLearnBeanCreate.h"
#import "Run+ARLearnBeanCreate.h"

@interface ARLDataCollectionWidget : UIView<UINavigationControllerDelegate, UIImagePickerControllerDelegate> {
//    BOOL withAudio;
//    BOOL withPicture;
//    BOOL withText;
//    BOOL withValue;
//    BOOL withVideo;
//    BOOL isVisible;
}

@property (nonatomic, readwrite) BOOL withAudio;
@property (nonatomic, readwrite) BOOL withPicture;
@property (nonatomic, readwrite) BOOL withText;
@property (nonatomic, readwrite) BOOL withValue;
@property (nonatomic, readwrite) BOOL withVideo;
@property (nonatomic, readwrite) BOOL isVisible;


@property (nonatomic, weak) GeneralItem* generalItem;
@property (nonatomic, weak) Run* run;
@property (nonatomic, strong) UITextField *valueTextField;
@property (strong, nonatomic) UIImagePickerController * imagePickerController;
@property (strong, nonatomic) UIViewController * generalItemViewController;

@property (nonatomic, strong) NSString *textDescription;
@property (nonatomic, strong) NSString *valueDescription;

- (id) init : (NSDictionary *) jsonDict viewController: (UIViewController*) viewController;

- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info;
@end
