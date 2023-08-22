#import "QNDeviceSDK.h"
#import "QNConfig.h"
#import "QNBleConnectionChangeProtocol.h"
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>

@interface RCT_EXTERN_MODULE(BleQnsdk, NSObject)
RCT_EXTERN_METHOD(buildUser:(NSString)birthday height:(NSInteger)height gender:(NSString)gender id:(NSString)id unit:(NSInteger)unit athleteType:(NSInteger)athleteType  resolver: (RCTPromiseResolveBlock)resolve
    rejecter: (RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(onStartDiscovery:(RCTPromiseResolveBlock)resolve
    rejecter: (RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(onStopDiscovery:(RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(fetchConnectedDeviceInfo)

+ (BOOL)requiresMainQueueSetup
{
    return NO;
}

@end
