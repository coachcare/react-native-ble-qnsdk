// #import <React/RCTBridgeModule.h>

// @interface BleQnsdk : NSObject <RCTBridgeModule>

// @end


// @interface RTNCalculator : NativeBleQNSDKSpecBase <NativeBleQNSDKSpec>
#import <CoreBluetooth/CoreBluetooth.h>
#import <Foundation/Foundation.h>

#import <BleManagerSpec/BleManagerSpec.h>

@interface BleManager : NativeBleManagerSpecBase <NativeBleManagerSpec>


@interface SpecChecker : NSObject
+ (BOOL)isSpecAvailable;
@end