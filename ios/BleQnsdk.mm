#import "QNDeviceSDK.h"
#import "QNConfig.h"
#import "QNBleConnectionChangeProtocol.h"
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>

@interface RCT_EXTERN_MODULE(BleQnsdk, NSObject)
RCT_EXTERN_METHOD(buildUser:(UserObject)user resolver: (RCTPromiseResolveBlock)resolve
    rejecter: (RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(onStartDiscovery:(NSString *)name resolver:(RCTPromiseResolveBlock)resolve
    rejecter: (RCTPromiseRejectBlock)reject)
    RCT_EXTERN_METHOD(onStopDiscovery)


+ (BOOL)requiresMainQueueSetup
{
    return NO;
}

@end