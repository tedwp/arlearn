//
//  ARLScanTagViewController.h
//  ARLearn
//
//  Created by Stefaan Ternier on 1/17/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "GeneralItem.h"

@interface ARLScanTagViewController : UIViewController 
@property (nonatomic, strong) NSNumber * id;
@property (weak, nonatomic) NSManagedObjectContext * moc;
@property (nonatomic, strong) GeneralItem * generalItem;
- (IBAction)scanTag:(id)sender;
@end
