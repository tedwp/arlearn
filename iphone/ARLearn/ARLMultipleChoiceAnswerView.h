//
//  ARLMultipleChoiceAnswerView.h
//  ARLearn
//
//  Created by Stefaan Ternier on 7/12/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ARLAnswerItemView.h"

@interface ARLMultipleChoiceAnswerView : UIView

@property (nonatomic, retain) NSMutableArray* dataArray;
@property (nonatomic, retain) NSMutableArray * answerViews;

- (id) initWith: (NSDictionary *) jsonDict;
- (int) height;
- (NSArray *) selectedIds;
@end
