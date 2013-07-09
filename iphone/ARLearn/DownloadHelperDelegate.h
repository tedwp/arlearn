//
//  DownloadHelperDelegate.h
//  ARLearn
//
//  Created by Stefaan Ternier on 4/9/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol DownloadHelperDelegate <NSObject>
@optional
- (void) didReceiveData: (NSData *) theData;
- (void) didReceiveFilename: (NSString *) aName;
- (void) dataDownloadFailed: (NSString *) reason;
- (void) dataDownloadAtPercent: (NSNumber *) aPercent;
@end

@interface DownloadHelper : NSObject {
    NSURLResponse *response;
    NSMutableData *data;
    NSString *urlString;
    NSURLConnection *urlconnection;
    id <DownloadHelperDelegate> delegate;
    BOOL isDownloading;
}

@property (retain) NSURLResponse *response;
@property (retain) NSURLConnection *urlconnection;
@property (retain) NSMutableData *data;
@property (retain) NSString *urlString;
@property (retain) id delegate;
@property (assign) BOOL isDownloading;
@property (retain) NSFileHandle* fileHandle;
@property (retain) NSString* filePath;

+ (DownloadHelper *) sharedInstance;
+ (void) download:(NSString *) aURLString;
+ (void) cancel;
@end
