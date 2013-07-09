//
//  ARLRunViewCell.h
//  ARLearn
//
//  Created by Stefaan Ternier on 1/13/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ARLRunViewCell : UITableViewCell

@property (nonatomic, strong) IBOutlet UILabel *runTitleLabel;
@property (weak, nonatomic) IBOutlet UILabel *runDetail;
@property (weak, nonatomic) IBOutlet UIImageView *icon;

@end
