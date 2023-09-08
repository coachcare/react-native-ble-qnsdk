export interface IYolandaUser {
    birthday: string;
    gender: string | "male" | "female";
    id: string;
    height: number;
    unit: number;
    athleteType: number;
}
export interface ConnectionStatus {
    status: string;
    error?: string;
    description?: string;
}
export interface TemporaryMeasurementResponse {
    weight: number;
}
export interface FinalMeasurementResponse extends TemporaryMeasurementResponse {
    basalMetabolicRate: number;
    visceralFatTanita: number;
    fatFreeMass: number;
    bodyFat: number;
    waterPercentage: number;
    skeletalMuscleRatio: number;
    muscleMass: number;
}
export interface YolandaDeviceInfo {
    id: string;
    name: string;
    modeId: string;
    bluetoothName: string;
    deviceType: string;
    maxUserNum: number;
    registeredUserNum: number;
    firmwareVer: number;
    hardwareVer: number;
    softwareVer: number;
}
export interface YolandaLoggingInfo {
    status: string;
    device?: YolandaDeviceInfo;
}
export interface ConnectionStatusEmitter {
    type: "connectionStatus";
    value: ConnectionStatus;
}
export interface TemporaryMeasurementEvent {
    type: "temporaryMeasurementReceived";
    value: TemporaryMeasurementResponse;
}
export interface FinalMeasurementEvent {
    type: "finalMeasurementReceived";
    value: FinalMeasurementResponse;
}
export interface ScaleEventChangeEvent {
    type: "scaleEventChange";
    value: number;
}
export interface ScaleStateChangeEvent {
    type: "scaleStateChange";
    value: number;
}
export interface DeviceInfoEvent {
    type: "deviceInfo";
    value: YolandaDeviceInfo;
}
export interface YolandaLoggingEvent {
    type: "logging";
    value: YolandaDeviceInfo;
}
export type YolandaEventEmitter = TemporaryMeasurementEvent | FinalMeasurementEvent | ScaleEventChangeEvent | ScaleStateChangeEvent | DeviceInfoEvent | ConnectionStatusEmitter | YolandaLoggingInfo;
export declare enum YolandaEventTypeEnum {
    DEVICE_INFO = "deviceInfo",
    LOGGING = "logging",
    SCALE_STATE_CHANGE = "scaleStateChange",
    SCALE_EVENT_CHANGE = "scaleEventChange",
    FINAL_MEASUREMENT_EVENT = "finalMeasurementReceived",
    TEMPORARY_MEASUREMENT_EVENT = "temporaryMeasurementReceived",
    CONNECTION_STATUS = "connectionStatus"
}
