#import "BleQnsdk.h"

#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>

@interface RCT_EXTERN_MODULE(QNSDKManager, NSObject)
    RCT_EXTERN_METHOD(onStartDiscovery)
    RCT_EXTERN_METHOD(onStopDiscovery)

+ (BOOL)requiresMainQueueSetup {
    return YES;
}
@end

@implementation BleQnsdk

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(sampleMethod:(NSString *)stringArgument numberParameter:(nonnull NSNumber *)numberArgument callback:(RCTResponseSenderBlock)callback)
{
    // TODO: Implement some actually useful functionality
    callback(@[[NSString stringWithFormat: @"numberArgument: %@ stringArgument: %@", numberArgument, stringArgument]]);
}

@end
