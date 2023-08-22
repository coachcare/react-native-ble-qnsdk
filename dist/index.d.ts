import { ConnectionStatus, IYolandaUser, MeasurementResponse, ScaleEventValue, YolandaEventEmitter } from "./types";
import { NativeEventEmitter } from "react-native";
declare const BleQnsdk: any;
declare const QNSDKEmitter: NativeEventEmitter;
declare function buildYolandaUser(user: IYolandaUser): Promise<number>;
declare function startYolandaScan(): Promise<void>;
declare function stopYolandaScan(): Promise<void>;
export { BleQnsdk, QNSDKEmitter, buildYolandaUser, startYolandaScan, stopYolandaScan, IYolandaUser, ConnectionStatus, MeasurementResponse, ScaleEventValue, YolandaEventEmitter, };
