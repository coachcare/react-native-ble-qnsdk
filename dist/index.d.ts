import { ConnectionStatus, ConnectionStatusEmitter, DeviceInfoEvent, FinalMeasurementEvent, FinalMeasurementResponse, IYolandaUser, ScaleEventChangeEvent, ScaleStateChangeEvent, TemporaryMeasurementEvent, TemporaryMeasurementResponse, YolandaDeviceInfo, YolandaEventEmitter, YolandaEventTypeEnum } from "./types";
import { NativeEventEmitter } from "react-native";
declare const BleQnsdk: any;
declare const QNSDKEmitter: NativeEventEmitter;
declare function buildYolandaUser(user: IYolandaUser): Promise<number>;
declare function startYolandaScan(): Promise<void>;
declare function stopYolandaScan(): Promise<void>;
declare function fetchConnectedDeviceInfo(): void;
export { BleQnsdk, QNSDKEmitter, buildYolandaUser, startYolandaScan, stopYolandaScan, fetchConnectedDeviceInfo, IYolandaUser, YolandaDeviceInfo, TemporaryMeasurementResponse, FinalMeasurementResponse, ConnectionStatus, TemporaryMeasurementEvent, FinalMeasurementEvent, ScaleEventChangeEvent, ScaleStateChangeEvent, DeviceInfoEvent, ConnectionStatusEmitter, YolandaEventEmitter, YolandaEventTypeEnum, };
