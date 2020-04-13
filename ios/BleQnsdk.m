#import "BleQnsdk.h"
#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(QNSDKManager, NSObject)
    RCT_EXTERN_METHOD(onStartDiscovery)
    RCT_EXTERN_METHOD(onStopDiscovery)
@end

@interface QNSDKManager (RCTExternModule) <RCTBridgeModule>
@end

@implementation QNSDKManager (RCTExternModule)
@end

