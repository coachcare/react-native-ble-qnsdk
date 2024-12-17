import { YolandaEventTypeEnum, } from "./types";
import { NativeModules, Platform } from "react-native";
var LINKING_ERROR = "The package 'react-native-ble-qnsdk' doesn't seem to be linked. Make sure: \n\n" +
    Platform.select({ ios: "- You have run 'pod install'\n", default: "" }) +
    "- You rebuilt the app after installing the package\n" +
    "- You are not using Expo Go\n";
var isTurboModuleEnabled = global.__turboModuleProxy != null;
var BleQNSDKModule = isTurboModuleEnabled
    ? require("./NativeBleQNSDK").default
    : NativeModules.BleQnsdk;
var BleQnsdk = NativeModules.BleQnsdk
    ? BleQNSDKModule
    : new Proxy({}, {
        get: function () {
            throw new Error(LINKING_ERROR);
        },
    });
function buildYolandaUser(user) {
    return BleQnsdk.buildUser(user.birthday, user.height, user.gender, user.id, user.unit, user.athleteType);
}
function startYolandaScan() {
    return BleQnsdk.onStartDiscovery();
}
function stopYolandaScan() {
    return BleQnsdk.onStopDiscovery();
}
function fetchConnectedDeviceInfo() {
    return BleQnsdk.fetchConnectedDeviceInfo();
}
function disconnectDevice() {
    return BleQnsdk.disconnectDevice();
}
export { BleQnsdk, buildYolandaUser, startYolandaScan, stopYolandaScan, fetchConnectedDeviceInfo, disconnectDevice, YolandaEventTypeEnum, };
//# sourceMappingURL=index.js.map