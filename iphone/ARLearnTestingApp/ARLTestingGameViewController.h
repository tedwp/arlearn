//
//  ARLTestingGameViewController.h
//  ARLearn
//
//  Created by Stefaan Ternier on 2/3/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ARLNetwork.h"

@interface ARLTestingGameViewController : UIViewController
- (IBAction)allParticipateGames:(id)sender;
@property (weak, nonatomic) IBOutlet UITextView *resultPane;

@end
