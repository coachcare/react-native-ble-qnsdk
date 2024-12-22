import BleQnsdk from './NativeBleQnsdk';

import {
  type ConnectionStatus,
  type ConnectionStatusEmitter,
  type DeviceInfoEvent,
  type FinalMeasurementEvent,
  type FinalMeasurementResponse,
  type IYolandaUser,
  type ScaleEventChangeEvent,
  type ScaleStateChangeEvent,
  type TemporaryMeasurementEvent,
  type TemporaryMeasurementResponse,
  type YolandaDeviceInfo,
  type YolandaEventEmitter,
  YolandaEventTypeEnum,
} from './types';

import { NativeModules, type EventSubscription } from 'react-native';


const BleQnsdk = require("./NativeBleQnsdk").default ?? NativeModules.BleQnsdk;

console.log("1",  NativeModules?.BleQnsdk)
console.log("2",  BleQnsdk)

function buildYolandaUser(user: IYolandaUser): Promise<void> {
  return BleQnsdk.buildUser(
    user.birthday,
    user.height,
    user.gender,
    user.id,
    user.unit,
    user.athleteType
  );
}

function startYolandaScan(): Promise<void> {
  return BleQnsdk.onStartDiscovery();
}

function stopYolandaScan(): Promise<void> {
  return BleQnsdk.onStopDiscovery();
}

function fetchConnectedDeviceInfo(): void {
  return BleQnsdk.fetchConnectedDeviceInfo();
}

function disconnectDevice(): Promise<void> {
  return BleQnsdk.disconnectDevice();
}

function onValueChanged(callback: any): EventSubscription {
  return BleQnsdk.onValueChanged(callback);
}

export {
  BleQnsdk,
  onValueChanged,
  buildYolandaUser,
  startYolandaScan,
  stopYolandaScan,
  fetchConnectedDeviceInfo,
  disconnectDevice,
  YolandaEventTypeEnum,
};
export type {
  IYolandaUser,
  YolandaDeviceInfo,
  TemporaryMeasurementResponse,
  FinalMeasurementResponse,
  ConnectionStatus,
  TemporaryMeasurementEvent,
  FinalMeasurementEvent,
  ScaleEventChangeEvent,
  ScaleStateChangeEvent,
  DeviceInfoEvent,
  ConnectionStatusEmitter,
  YolandaEventEmitter,
};
