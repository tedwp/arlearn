//
//  ARLMultipleChoiceTestViewController.h
//  ARLearn
//
//  Created by Stefaan Ternier on 1/17/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "GeneralItem.h"



@interface ARLMultipleChoiceTestViewController : UIViewController <UIPickerViewDelegate, UIPickerViewDataSource>

@property (strong, nonatomic) GeneralItem * generalItem;

//@property (nonatomic, strong) NSNumber * id;
//@property (weak, nonatomic) NSManagedObjectContext * moc;
//@property (weak, nonatomic) NSDictionary * jsonDict;
@property (weak, nonatomic) IBOutlet UINavigationItem *headerText;
@property (weak, nonatomic) IBOutlet UIWebView *webView;
@property (weak, nonatomic) IBOutlet UIPickerView *pickerView;
@property (nonatomic, retain) NSMutableArray *dataArray;

@end
