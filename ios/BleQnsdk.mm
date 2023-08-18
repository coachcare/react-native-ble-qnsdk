#import "QNDeviceSDK.h"
#import "QNConfig.h"
#import "QNBleConnectionChangeProtocol.h"
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>

@interface RCT_EXTERN_MODULE(BleQnsdk, NSObject)
RCT_EXTERN_METHOD(buildUser:(NSString *)name birthday:(NSString *)birthday height:(NSInteger *)height gender:(NSString *)gender id:(NSString *)id unit:(NSInteger *)unit athleteType:(NSInteger *)athleteType resolver: (RCTPromiseResolveBlock)resolve
    rejecter: (RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(onStartDiscovery:(NSString *)name resolver:(RCTPromiseResolveBlock)resolve
    rejecter: (RCTPromiseRejectBlock)reject)
    RCT_EXTERN_METHOD(onStopDiscovery)


+ (BOOL)requiresMainQueueSetup
{
    return NO;
}

@end