// #import "QNDeviceSDK.h"
// #import "QNConfig.h"
// #import "QNBleConnectionChangeProtocol.h"
// #import <React/RCTBridgeModule.h>
// #import <React/RCTEventEmitter.h>

// @implementation SpecChecker

// + (BOOL)isSpecAvailable {
// #ifdef RCT_NEW_ARCH_ENABLED
//     return YES;
// #else
//     return NO;
// #endif
// }

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

#import "BleQnsdk.h"
#import "QNDeviceSDK.h"
#import "QNConfig.h"
#import "QNBleConnectionChangeProtocol.h"
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>

// Define a constant for the module name
static NSString *const BleQnsdkKey = @"BleQNSDK";

@interface BleQnsdk ()
@property (nonatomic, strong) QNDeviceSDK *deviceSDK;
@end

@implementation BleQnsdk

RCT_EXPORT_MODULE(BleQnsdk)

- (instancetype)init {
  if (self = [super init]) {
    _deviceSDK = [[QNDeviceSDK alloc] init];
  }
  return self;
}

// TurboModule-specific implementation
- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:(const facebook::react::ObjCTurboModule::InitParams &)params {
  return std::make_shared<facebook::react::BleQnsdkSpecJSI>(params);
}

// Method to build a user
RCT_EXPORT_METHOD(buildUser:(NSString *)birthday
                  height:(NSInteger)height
                  gender:(NSString *)gender
                  id:(NSString *)userId
                  unit:(NSInteger)unit
                  athleteType:(NSInteger)athleteType
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
  @try {
    QNUser *user = [[QNUser alloc] init];
    user.birthday = birthday;
    user.height = height;
    user.gender = gender;
    user.userId = userId;
    user.unit = unit;
    user.athleteType = athleteType;

    resolve(@{@"success": @(YES)});
  } @catch (NSException *exception) {
    reject(@"build_user_error", exception.reason, nil);
  }
}

// Method to start BLE discovery
RCT_EXPORT_METHOD(onStartDiscovery:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
  @try {
    [_deviceSDK startDiscovery:^(NSArray<QNDevice *> *devices) {
      NSMutableArray *deviceArray = [NSMutableArray new];
      for (QNDevice *device in devices) {
        [deviceArray addObject:@{@"name": device.name, @"mac": device.mac}];
      }
      resolve(deviceArray);
    }];
  } @catch (NSException *exception) {
    reject(@"start_discovery_error", exception.reason, nil);
  }
}

// Method to stop BLE discovery
RCT_EXPORT_METHOD(onStopDiscovery:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
  @try {
    [_deviceSDK stopDiscovery];
    resolve(@{@"success": @(YES)});
  } @catch (NSException *exception) {
    reject(@"stop_discovery_error", exception.reason, nil);
  }
}

// Method to fetch connected device information
RCT_EXPORT_METHOD(fetchConnectedDeviceInfo) {
  NSArray<QNDevice *> *connectedDevices = [_deviceSDK getConnectedDevices];
  for (QNDevice *device in connectedDevices) {
    NSLog(@"Connected Device: %@, MAC: %@", device.name, device.mac);
  }
}

// Method to disconnect device
RCT_EXPORT_METHOD(disconnectDevice) {
  [_deviceSDK disconnectAllDevices];
}

// Required methods for RCTBridgeModule
- (dispatch_queue_t)methodQueue {
  return dispatch_get_main_queue();
}

+ (BOOL)requiresMainQueueSetup {
  return YES;
}

@end
