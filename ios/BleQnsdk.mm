#import "BleQnsdk.h"
#import "QNDeviceSDK.h"
#import "QNConfig.h"
#import "QNBleConnectionChangeProtocol.h"
#import "BleQnsdk-Swift.h" // Auto-generated Swift header

@implementation BleQnsdk
RCT_EXPORT_MODULE()

// - (NSNumber *)multiply:(double)a b:(double)b {
//     NSNumber *result = @(a * b);

//     return result;
// }

RCT_EXTERN_METHOD(buildUser:(NSString)birthday height:(NSInteger)height gender:(NSString)gender id:(NSString)id unit:(NSInteger)unit athleteType:(NSInteger)athleteType  resolver: (RCTPromiseResolveBlock)resolve
    rejecter: (RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(onStartDiscovery:(RCTPromiseResolveBlock)resolve
    rejecter: (RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(onStopDiscovery:(RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(fetchConnectedDeviceInfo)
RCT_EXTERN_METHOD(disconnectDevice)
[self emitOnValueChanged:@(result)];

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params
{
    return std::make_shared<facebook::react::NativeBleQnsdkSpecJSI>(params);
}

@end
