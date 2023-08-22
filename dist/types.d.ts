export interface IYolandaUser {
    birthday: string;
    gender: "male" | "female";
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
export interface MeasurementResponse {
    weight: number;
    basalMetabolicRate?: number;
    visceralFatTanita?: number;
    fatFreeMass?: number;
    bodyFat?: number;
    waterPercentage?: number;
    skeletalMuscleRatio?: number;
    muscleMass?: number;
}
export interface ScaleEventValue {
    value: number;
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
export interface YolandaEventEmitter {
    connectionStatus?: ConnectionStatus;
    measurementReceived?: MeasurementResponse;
    scaleEventChange?: ScaleEventValue;
    scaleStateChange?: ScaleEventValue;
    deviceInfo?: DeviceInfo;
}
