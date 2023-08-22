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
  temporaryMeasurementReceived?: TemporaryMeasurementResponse;
  finalMeasurementReceived?: FinalMeasurementResponse;
  scaleEventChange?: ScaleEventValue;
  scaleStateChange?: ScaleEventValue;
  deviceInfo?: DeviceInfo;
}

// export interface ConnectionStatusEmitter {
//     connectionStatus: ConnectionStatus;
//   }
  
//   export interface TemporaryMeasurementEvent {
//     temporaryMeasurementReceived: TemporaryMeasurementResponse;
//   }
  
//   export interface FinalMeasurementEvent {
//     finalMeasurementReceived: FinalMeasurementResponse;
//   }
  
//   export interface ScaleEventChangeEvent {
//     scaleEventChange: ScaleEventValue;
//   }
  
//   export interface ScaleStateChangeEvent {
//     scaleStateChange: ScaleEventValue;
//   }
  
//   export interface DeviceInfoEvent {
//     deviceInfo: DeviceInfo;
//   }
  
//   export type YolandaEvent =
//     | YolandaEventEmitter
//     | TemporaryMeasurementEvent
//     | FinalMeasurementEvent
//     | ScaleEventChangeEvent
//     | ScaleStateChangeEvent
//     | DeviceInfoEvent;