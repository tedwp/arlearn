//
//  ARLGeneralItemTableViewCell.m
//  ARLearn
//
//  Created by Stefaan Ternier on 7/22/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLGeneralItemTableViewCell.h"

@implementation ARLGeneralItemTableViewCell


@synthesize giTitleLabel = _giTitleLabel;
@synthesize icon;

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        // Initialization code
    }
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
