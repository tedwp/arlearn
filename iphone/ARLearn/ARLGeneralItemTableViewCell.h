//
//  ARLGeneralItemTableViewCell.h
//  ARLearn
//
//  Created by Stefaan Ternier on 7/22/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ARLGeneralItemTableViewCell : UITableViewCell

@property (nonatomic, strong) IBOutlet UILabel *giTitleLabel;
@property (weak, nonatomic) IBOutlet UIImageView *icon;

@end
