#import "BleQnsdk.h"
#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(QNSDKManager, NSObject)
RCT_EXTERN_METHOD(buildUser:(NSString *)name birthday:(NSString *)birthday height:(NSInteger *)height gender:(NSString *)gender id:(NSString *)id unit:(NSInteger *)unit athleteType:(NSInteger *)athleteType resolver: (RCTPromiseResolveBlock)resolve
    rejecter: (RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(onStartDiscovery:(NSString *)name resolver:(RCTPromiseResolveBlock)resolve
    rejecter: (RCTPromiseRejectBlock)reject)
    RCT_EXTERN_METHOD(onStopDiscovery)

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

+ (BOOL)requiresMainQueueSetup
{
    return YES;
}

@end

@interface QNSDKManager (RCTExternModule) <RCTBridgeModule>
@end

@implementation QNSDKManager (RCTExternModule)
@end

