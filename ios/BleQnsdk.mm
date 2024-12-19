#import "BleQnsdk.h"
#import "QNDeviceSDK.h"
#import "QNConfig.h"
#import "QNBleConnectionChangeProtocol.h"
// #import <React/RCTBridgeModule.h>
// #import <React/RCTEventEmitter.h>

#import <React/RCTBridge.h>
#import <React/RCTUtils.h>
#import <React/RCTTurboModuleManager.h>

// Define a constant for the module name
static NSString *const BleQnsdkKey = @"BleQNSDK";

@interface BleQnsdk ()
@property (nonatomic, strong) QNDeviceSDK *deviceSDK;
@end

@implementation BleQnsdk

RCT_EXPORT_MODULE(BleQNSDK)

- (instancetype)init {
  if (self = [super init]) {
    _deviceSDK = [[QNDeviceSDK alloc] init];
  }
  return self;
}

#ifdef RCT_NEW_ARCH_ENABLED
- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:(const facebook::react::ObjCTurboModule::InitParams &)params {
  return std::make_shared<facebook::react::NativeBleQNSDKSpecJSI>(params);
}
#endif

+ (NSString *)moduleName {
  return @"BleQNSDK";
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

// Method to handle value changes
RCT_EXPORT_METHOD(onValueChanged:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
  @try {
    // Implement the logic to handle value changes
    resolve(@{@"success": @(YES)});
  } @catch (NSException *exception) {
    reject(@"value_changed_error", exception.reason, nil);
  }
}

// Required methods for RCTBridgeModule
// - (dispatch_queue_t)methodQueue {
//   return dispatch_get_main_queue();
// }

+ (BOOL)requiresMainQueueSetup {
  return NO;
}

@end

Class BleQNSDKCls(void) {
  return BleQnsdk.class;
}
