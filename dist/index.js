"use strict";
var _a;
Object.defineProperty(exports, "__esModule", { value: true });
exports.YolandaEventTypeEnum = exports.BleQnsdk = void 0;
exports.onValueChanged = onValueChanged;
exports.buildYolandaUser = buildYolandaUser;
exports.startYolandaScan = startYolandaScan;
exports.stopYolandaScan = stopYolandaScan;
exports.fetchConnectedDeviceInfo = fetchConnectedDeviceInfo;
exports.disconnectDevice = disconnectDevice;
const types_1 = require("./types");
Object.defineProperty(exports, "YolandaEventTypeEnum", { enumerable: true, get: function () { return types_1.YolandaEventTypeEnum; } });
const react_native_1 = require("react-native");
const BleQnsdk = (_a = require("./NativeBleQnsdk").default) !== null && _a !== void 0 ? _a : react_native_1.NativeModules.BleQnsdk;
exports.BleQnsdk = BleQnsdk;
console.log("11111", react_native_1.NativeModules === null || react_native_1.NativeModules === void 0 ? void 0 : react_native_1.NativeModules.BleQnsdk);
console.log("22222", BleQnsdk);
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
function onValueChanged(callback) {
    return BleQnsdk.onValueChanged(callback);
}
