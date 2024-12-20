// #import <React/RCTBridgeModule.h>

// @interface BleQnsdk : NSObject <RCTBridgeModule>

// @end

#import <BleQnsdkSpec/BleQnsdkSpec.h>

NS_ASSUME_NONNULL_BEGIN

- @interface BleQnsdk : NSObject <NativeBleQnsdkSpec>
+ @interface BleQnsdk : NativeBleQnsdkSpecBase <NativeBleQnsdkSpec>

@end

NS_ASSUME_NONNULL_END