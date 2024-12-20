// #import "QNDeviceSDK.h"
// #import "QNConfig.h"
// #import "QNBleConnectionChangeProtocol.h"
// #import <React/RCTBridgeModule.h>
// #import <React/RCTEventEmitter.h>

// @interface RCT_EXTERN_MODULE(BleQnsdk, NSObject)
// RCT_EXTERN_METHOD(buildUser:(NSString)birthday height:(NSInteger)height gender:(NSString)gender id:(NSString)id unit:(NSInteger)unit athleteType:(NSInteger)athleteType  resolver: (RCTPromiseResolveBlock)resolve
//     rejecter: (RCTPromiseRejectBlock)reject)
// RCT_EXTERN_METHOD(onStartDiscovery:(RCTPromiseResolveBlock)resolve
//     rejecter: (RCTPromiseRejectBlock)reject)
// RCT_EXTERN_METHOD(onStopDiscovery:(RCTPromiseResolveBlock)resolve
//                   rejecter: (RCTPromiseRejectBlock)reject)
// RCT_EXTERN_METHOD(fetchConnectedDeviceInfo)
// RCT_EXTERN_METHOD(disconnectDevice)

// - (dispatch_queue_t)methodQueue
// {
//     return dispatch_get_main_queue();
// }

// + (BOOL)requiresMainQueueSetup
// {
//     return YES;
// }

// @end


// #import "BleQnsdk.h"

// // @implementation RTNCalculator

// // RCT_EXPORT_MODULE()

// // - (void)add:(double)a b:(double)b resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject {
// //     NSNumber *result = [[NSNumber alloc] initWithInteger:a+b];
// //     resolve(result);
// // +   [self emitOnValueChanged:@(result)];
// // }

// // - (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
// //     (const facebook::react::ObjCTurboModule::InitParams &)params
// // {
// //     return std::make_shared<facebook::react::NativeRTNCalculatorSpecJSI>(params);
// // }

// // @end

// #import "BleQnsdk.h"

// @implementation BleQNSDK

// RCT_EXPORT_MODULE()

// - (void)add:(double)a b:(double)b resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject {
//     NSNumber *result = [[NSNumber alloc] initWithInteger:a+b];
//     resolve(result);
// }

// - (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
//     (const facebook::react::ObjCTurboModule::InitParams &)params
// {
//     return std::make_shared<facebook::react::NativeRTNCalculatorSpecJSI>(params);
// }

// @end

#import "QNDeviceSDK.h"
#import "QNConfig.h"
#import "QNBleConnectionChangeProtocol.h"
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>
#import <ReactCommon/RCTTurboModule.h>
#import <ReactCommon/TurboModuleUtils.h>
#import <react/renderer/core/ReactPrimitives.h>
#import <memory>

// @interface BleQnsdk : NSObject <RCTBridgeModule>
// @end

@implementation BleQnsdk

RCT_EXPORT_MODULE()

// Existing methods

RCT_EXTERN_METHOD(buildUser:(NSString)birthday height:(NSInteger)height gender:(NSString)gender id:(NSString)id unit:(NSInteger)unit athleteType:(NSInteger)athleteType  resolver: (RCTPromiseResolveBlock)resolve
    rejecter: (RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(onStartDiscovery:(RCTPromiseResolveBlock)resolve
    rejecter: (RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(onStopDiscovery:(RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(fetchConnectedDeviceInfo)
RCT_EXTERN_METHOD(disconnectDevice)
[self emitOnValueChanged:@(result)];

// Required for React Native modules
// - (dispatch_queue_t)methodQueue
// {
//     return dispatch_get_main_queue();
// }

// + (BOOL)requiresMainQueueSetup
// {
//     return YES;
// }

// TurboModule support
- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params
{
    return std::make_shared<facebook::react::NativeBleQnsdkSpecJSI>(params);
}

@end
