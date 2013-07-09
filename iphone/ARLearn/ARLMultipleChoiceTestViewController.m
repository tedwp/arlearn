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
//@synthesize id = _id;
//@synthesize moc = _moc;
@synthesize headerText;
@synthesize webView, pickerView;
@synthesize dataArray;


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
	// Do any additional setup after loading the view.
//    self.moc = [ARLDatabaseGeneralItem item:self.id];
//    self.headerText.title = [self.moc valueForKey:@"name"];
    NSDictionary * jsonDict = [NSKeyedUnarchiver unarchiveObjectWithData:self.generalItem.json];
    dataArray = [[NSMutableArray alloc] init];
    for (NSDictionary *ans in [jsonDict objectForKey:@"answers"]) {
             [dataArray addObject:[ans objectForKey:@"answer"]];
        }
       
    [self.webView loadHTMLString:self.generalItem.richText baseURL:nil];
    
    [pickerView setDataSource: self];
    [pickerView setDelegate: self];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


// Number of components.
-(NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView{
    return 1;
}

// Total rows in our component.
-(NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component{
    return [dataArray count];
}

// Display each row's data.
-(NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component{
    return [dataArray objectAtIndex: row];
}

// Do something with the selected row.
-(void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component{
    NSLog(@"You selected this: %@", [dataArray objectAtIndex: row]);
}


@end
