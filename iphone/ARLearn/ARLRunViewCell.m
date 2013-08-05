//
//  ARLRunViewCell.m
//  ARLearn
//
//  Created by Stefaan Ternier on 1/13/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLRunViewCell.h"

@implementation ARLRunViewCell

@synthesize runTitleLabel = _runTitleLabel;
@synthesize runDetail = _runDetail;
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
