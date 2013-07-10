//
//  ARLScanObjectViewController.m
//  ARLearn
//
//  Created by Stefaan Ternier on 7/9/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLScanObjectViewController.h"
#import "ARLNetwork.h"

@interface ARLScanObjectViewController ()

@end

@implementation ARLScanObjectViewController

@synthesize generalItem = _generalItem;
@synthesize run = _run;

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
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)scanTagButton:(id)sender {
    NSLog(@"Pressed button");
    
    ZBarReaderViewController *reader = [ZBarReaderViewController new];
    reader.readerDelegate = self;
    reader.supportedOrientationsMask = ZBarOrientationMaskAll;
    
    ZBarImageScanner *scanner = reader.scanner;
    // TODO: (optional) additional reader configuration here
    
    // EXAMPLE: disable rarely used I2/5 to improve performance
    [scanner setSymbology: ZBAR_I25
                   config: ZBAR_CFG_ENABLE
                       to: 0];
    
    // present and release the controller
    [self presentModalViewController: reader
                            animated: YES];
    
//    ZXingWidgetController *widController = [[ZXingWidgetController alloc] initWithDelegate:self showCancel:YES OneDMode:NO];
//    QRCodeReader* qRCodeReader = [[QRCodeReader alloc] init];
//
//    NSMutableSet *readers = [[NSMutableSet alloc ] init];
//    MultiFormatReader* reader = [[MultiFormatReader alloc] init];
//    [readers addObject:qRCodeReader];
//    
//    widController.readers = readers;
//    
//    NSBundle *mainBundle = [NSBundle mainBundle];
//    widController.soundToPlay = [NSURL fileURLWithPath:[mainBundle pathForResource:@"beep-beep" ofType:@"aiff"] isDirectory:NO];
//    
//    [self presentViewController:widController animated:YES completion:nil];

}

- (void) imagePickerController: (UIImagePickerController*) reader
 didFinishPickingMediaWithInfo: (NSDictionary*) info
{
    // ADD: get the decode results
    id<NSFastEnumeration> results =
    [info objectForKey: ZBarReaderControllerResults];
    ZBarSymbol *symbol = nil;
    for(symbol in results)
        break;
    NSLog(@"scanned %@", symbol.data);
    [ARLNetwork publishAction:self.run.runId action:symbol.data itemId:self.generalItem.id itemType:self.generalItem.type];
        
    [reader dismissModalViewControllerAnimated: YES];
}

//- (void)zxingController:(ZXingWidgetController*)controller didScanResult:(NSString *)result {
//    NSLog(@"code %@", result);
//           [self.presentingViewController dismissViewControllerAnimated:YES completion:nil];
//   
//    
//}
//
//- (void)zxingControllerDidCancel:(ZXingWidgetController*)controller {
//    [self dismissModalViewControllerAnimated:NO];
//}

@end
