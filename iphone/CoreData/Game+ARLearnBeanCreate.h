//
//  Game+ARLearnBeanCreate.h
//  ARLearn
//
//  Created by Stefaan Ternier on 2/3/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "Game.h"
#import "Run.h"

@interface Game (ARLearnBeanCreate)

+ (Game *) gameWithDictionary: (NSDictionary *) gameDict inManagedObjectContext: (NSManagedObjectContext * ) context;
+ (Game *) retrieveGame: (NSNumber *) gameId inManagedObjectContext: (NSManagedObjectContext * ) context;
@end
