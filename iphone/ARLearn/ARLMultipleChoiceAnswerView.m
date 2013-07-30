//
//  ARLMultipleChoiceAnswerView.m
//  ARLearn
//
//  Created by Stefaan Ternier on 7/12/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "ARLMultipleChoiceAnswerView.h"

@implementation ARLMultipleChoiceAnswerView 


@synthesize dataArray = _dataArray;
@synthesize answerViews =_answerViews;


- (id) initWith: (NSDictionary *) jsonDict singleChoice: (BOOL) isSingleChoice{
    if (self = [super init]) {
        _isSingleChoice = isSingleChoice;
        self.backgroundColor = [UIColor clearColor]; //clearColor
        self.translatesAutoresizingMaskIntoConstraints = NO;
        
        _dataArray = [[NSMutableArray alloc] init];
        _answerViews =[[NSMutableArray alloc] init];
        NSString* vConstraint = @"V:|";
        //@"V:|-[webView]-[answerView(==100)]-|"
        NSMutableDictionary *viewsDictionary = [[NSMutableDictionary alloc] init];
        for (NSDictionary *ans in [jsonDict objectForKey:@"answers"]) {
            [_dataArray addObject:[ans objectForKey:@"answer"]];
            NSLog(@"ans %@", ans);
            vConstraint = [vConstraint stringByAppendingString:[NSString stringWithFormat:@"[_%@(==29)]", [ans objectForKey:@"id"]]];
            
            
            ARLAnswerItemView* ansItem = [[ARLAnswerItemView alloc] init];
            ansItem.delegate = self;
            ansItem.identifier = [ans objectForKey:@"id"];
            [_answerViews addObject:ansItem];
            
            [ansItem setText:[ans objectForKey:@"answer"]];
            [self addSubview:ansItem];
            [viewsDictionary setObject:ansItem forKey:[NSString stringWithFormat:@"_%@",[ans objectForKey:@"id"]]];
            NSString* hConstraint = [NSString stringWithFormat:@"H:|[_%@]|", [ans objectForKey:@"id"]];
//            NSLog(@"hConstraint: %@", hConstraint);

            [self addConstraints:[NSLayoutConstraint
                                  constraintsWithVisualFormat:hConstraint
                                  options:0
                                  metrics:nil
                                  views:viewsDictionary]];
        }
        vConstraint = [vConstraint stringByAppendingFormat:@"|"];
        NSLog(@"constraint: %@", vConstraint);
//        
        [self addConstraints:[NSLayoutConstraint
                              constraintsWithVisualFormat:vConstraint
                              options:0
                              metrics:nil
                              views:viewsDictionary]];
       

        
    }
    return self;
}

- (int) height {
    return [_dataArray count]*29;
}

- (void) answerClicked: (NSString *) identifier{
    NSLog(@"clicked on %@", identifier);
    if (!_isSingleChoice) return;
    for (ARLAnswerItemView* ansItemView in self.answerViews) {
        if (![identifier isEqualToString:ansItemView.identifier]){
            [ansItemView disable];
        }
    }
}

- (NSArray *) selectedIds {
    NSMutableArray* result = [[NSMutableArray alloc] init];
    for (ARLAnswerItemView* answerView in self.answerViews) {
//        NSLog(@"key %@ selected %d", key, );
        if ([answerView isSelected]) {
            [result addObject:answerView.identifier];

        }
    }

    return result;
}


@end
