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
export interface DeviceInfo {
    mac: string;
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
    value: DeviceInfo;
}
export type YolandaEventEmitter = TemporaryMeasurementEvent | FinalMeasurementEvent | ScaleEventChangeEvent | ScaleStateChangeEvent | DeviceInfoEvent | ConnectionStatusEmitter;
