// #import <React/RCTBridgeModule.h>

// @interface BleQnsdk : NSObject <RCTBridgeModule>

// @end


// @interface RTNCalculator : NativeBleQNSDKSpecBase <NativeBleQNSDKSpec>

#import <React/RCTBridgeModule.h>
#import <ReactCommon/TurboModule.h>
#import <ReactCommon/RCTTurboModule.h>

@interface BleQnsdk : NSObject <RCTBridgeModule, RCTTurboModule>

// Add method declarations here if needed, though most of the logic should be in the .mm file.

@end
