#import <UIKit/UIKit.h>
#import "ZXingWidgetController.h"

@interface RootViewController : UIViewController <ZXingDelegate> {
    IBOutlet UITextView *resultsView;
    NSString *resultsToDisplay;
}
@property (nonatomic, retain) IBOutlet UITextView *resultsView;
@property (nonatomic, copy) NSString *resultsToDisplay;

- (IBAction)scanPressed:(id)sender;
@end
