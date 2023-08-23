"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.YolandaEventTypeEnum = exports.fetchConnectedDeviceInfo = exports.stopYolandaScan = exports.startYolandaScan = exports.buildYolandaUser = exports.QNSDKEmitter = exports.BleQnsdk = void 0;
const types_1 = require("./types");
Object.defineProperty(exports, "YolandaEventTypeEnum", { enumerable: true, get: function () { return types_1.YolandaEventTypeEnum; } });
const react_native_1 = require("react-native");
const LINKING_ERROR = `The package 'react-native-ble-qnsdk' doesn't seem to be linked. Make sure: \n\n` +
    react_native_1.Platform.select({ ios: "- You have run 'pod install'\n", default: "" }) +
    "- You rebuilt the app after installing the package\n" +
    "- You are not using Expo Go\n";
const BleQnsdk = react_native_1.NativeModules.BleQnsdk
    ? react_native_1.NativeModules.BleQnsdk
    : new Proxy({}, {
        get() {
            throw new Error(LINKING_ERROR);
        },
    });
exports.BleQnsdk = BleQnsdk;
const QNSDKEmitter = new react_native_1.NativeEventEmitter(BleQnsdk);
exports.QNSDKEmitter = QNSDKEmitter;
function buildYolandaUser(user) {
    return BleQnsdk.buildUser(user.birthday, user.height, user.gender, user.id, user.unit, user.athleteType);
}
exports.buildYolandaUser = buildYolandaUser;
function startYolandaScan() {
    return BleQnsdk.onStartDiscovery();
}
exports.startYolandaScan = startYolandaScan;
function stopYolandaScan() {
    return BleQnsdk.onStopDiscovery();
}
exports.stopYolandaScan = stopYolandaScan;
function fetchConnectedDeviceInfo() {
    return BleQnsdk.fetchConnectedDeviceInfo();
}
exports.fetchConnectedDeviceInfo = fetchConnectedDeviceInfo;
