"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.YolandaEventTypeEnum = void 0;
var YolandaEventTypeEnum;
(function (YolandaEventTypeEnum) {
    YolandaEventTypeEnum["DEVICE_INFO"] = "deviceInfo";
    YolandaEventTypeEnum["SCALE_STATE_CHANGE"] = "scaleStateChange";
    YolandaEventTypeEnum["SCALE_EVENT_CHANGE"] = "scaleEventChange";
    YolandaEventTypeEnum["FINAL_MEASUREMENT_EVENT"] = "finalMeasurementReceived";
    YolandaEventTypeEnum["TEMPORARY_MEASUREMENT_EVENT"] = "temporaryMeasurementReceived";
    YolandaEventTypeEnum["CONNECTION_STATUS"] = "connectionStatus";
})(YolandaEventTypeEnum || (exports.YolandaEventTypeEnum = YolandaEventTypeEnum = {}));
