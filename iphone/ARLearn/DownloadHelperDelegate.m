//
//  DownloadHelperDelegate.m
//  ARLearn
//
//  Created by Stefaan Ternier on 4/9/13.
//  Copyright (c) 2013 Stefaan Ternier. All rights reserved.
//

#import "DownloadHelperDelegate.h"
#define DELEGATE_CALLBACK(X, Y) if (sharedInstance.delegate && [sharedInstance.delegate respondsToSelector:@selector(X)]) [sharedInstance.delegate performSelector:@selector(X) withObject:Y];
#define NUMBER(X) [NSNumber numberWithFloat:X]

static DownloadHelper *sharedInstance = nil;

@implementation DownloadHelper
@synthesize response;
@synthesize data;
@synthesize delegate;
@synthesize urlString;
@synthesize urlconnection;
@synthesize isDownloading;
@synthesize fileHandle;
@synthesize filePath;

- (void) start
{
    self.isDownloading = NO;
    
    NSURL *url = [NSURL URLWithString:self.urlString];
    if (!url)
    {
        NSString *reason = [NSString stringWithFormat:@"Could not create URL from string %@", self.urlString];
        DELEGATE_CALLBACK(dataDownloadFailed:, reason);
        return;
    }
    
    NSMutableURLRequest *theRequest = [NSMutableURLRequest requestWithURL:url];
    if (!theRequest)
    {
        NSString *reason = [NSString stringWithFormat:@"Could not create URL request from string %@", self.urlString];
        DELEGATE_CALLBACK(dataDownloadFailed:, reason);
        return;
    }
    
    self.urlconnection = [[NSURLConnection alloc] initWithRequest:theRequest delegate:self];
    if (!self.urlconnection)
    {
        NSString *reason = [NSString stringWithFormat:@"URL connection failed for string %@", self.urlString];
        DELEGATE_CALLBACK(dataDownloadFailed:, reason);
        return;
    }
    
    self.isDownloading = YES;
    
    // Create the new data object
    self.data = [NSMutableData data];
    self.response = nil;
    
    [self.urlconnection scheduleInRunLoop:[NSRunLoop currentRunLoop] forMode:NSRunLoopCommonModes];
}

- (void) cleanup
{
    self.data = nil;
    self.response = nil;
    self.urlconnection = nil;
    self.urlString = nil;
    self.isDownloading = NO;
}

- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)aResponse
{
    // store the response information
    self.response = aResponse;

    // Check for bad connection
    if ([aResponse expectedContentLength] < 0)
    {
        NSHTTPURLResponse* httpResponse = (NSHTTPURLResponse*)response;
        int code = [httpResponse statusCode];
        
        NSString *reason = [NSString stringWithFormat:@"Invalid URL [%@] code %d", self.urlString, code];
        DELEGATE_CALLBACK(dataDownloadFailed:, reason);
        [connection cancel];
        [self cleanup];
        return;
    }
    [DownloadHelper sharedInstance].filePath = [NSString stringWithFormat:@"%@/%@",  [DownloadHelper sharedInstance].filePath , [aResponse suggestedFilename] ];

    if ([aResponse suggestedFilename])
        DELEGATE_CALLBACK(didReceiveFilename:, [aResponse suggestedFilename]);
}

- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)theData
{
    // append the new data and update the delegate
//    filename = [[NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0] stringByAppendingPathComponent save_name];
    if (self.fileHandle) {
        [self.fileHandle writeData: data];
        NSLog(@"writing");
    } else {
        [theData writeToFile:self.filePath atomically:YES];
        self.fileHandle =[NSFileHandle fileHandleForUpdatingAtPath: self.filePath];
        NSLog(@"first write");        
    }
    
    if ([[NSFileManager defaultManager] fileExistsAtPath:self.filePath])
    {
//              NSDictionary *fileAttributes = [[NSFileManager defaultManager] attributesOfItemAtPath:self.filePath error:nil];
//        
//        NSNumber *fileSizeNumber = [fileAttributes objectForKey:NSFileSize];
        NSLog(@"file size %lld ", [self.fileHandle offsetInFile]);
        
    }
//    [self.data appendData:theData];
    if (self.response)
    {
        float expectedLength = [self.response expectedContentLength];
        float currentLength = self.data.length;
        float percent = currentLength / expectedLength;
        DELEGATE_CALLBACK(dataDownloadAtPercent:, NUMBER(percent));
    }
}

- (void)connectionDidFinishLoading:(NSURLConnection *)connection
{
    // finished downloading the data, cleaning up
    self.response = nil;
    [self.fileHandle closeFile];
    // Delegate is responsible for releasing data
    if (self.delegate)
    {
        NSData *theData = self.data;
        DELEGATE_CALLBACK(didReceiveData:, theData);
    }
    [self.urlconnection unscheduleFromRunLoop:[NSRunLoop currentRunLoop] forMode:NSRunLoopCommonModes];
    [self cleanup];
}

- (void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error
{
    self.isDownloading = NO;
    NSLog(@"Error: Failed connection, %@", [error localizedDescription]);
    DELEGATE_CALLBACK(dataDownloadFailed:, @"Failed Connection");
    [self cleanup];
}

+ (DownloadHelper *) sharedInstance
{
    if(!sharedInstance) sharedInstance = [[self alloc] init];
    return sharedInstance;
}

+ (void) download:(NSString *) aURLString
{
    if (sharedInstance.isDownloading)
    {
        NSLog(@"Error: Cannot start new download until current download finishes");
        DELEGATE_CALLBACK(dataDownloadFailed:, @"");
        return;
    }
    
    sharedInstance.urlString = aURLString;
    [sharedInstance start];
}

+ (void) cancel
{
    if (sharedInstance.isDownloading) [sharedInstance.urlconnection cancel];
}
@end