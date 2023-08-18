import { NativeEventEmitter, NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-ble-qnsdk' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const BleQnsdk = NativeModules.BleQnsdk
  ? NativeModules.BleQnsdk
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

const QnsSDKEmitter = new NativeEventEmitter(BleQnsdk);

interface IYolandaUser {
  birthday: string;
  gender: 'male' | 'female';
  id: string;
  height: number;
  unit: number;
  athleteType: number;
}

function buildUser(user: IYolandaUser): Promise<number> {
  return BleQnsdk.buildUser(user);
}

function onStartDiscovery(): Promise<void> {
  return BleQnsdk.onStartDiscovery();
}

function onStopDiscovery(): void {
  return BleQnsdk.onStopDiscovery();
}

export {
  BleQnsdk,
  QnsSDKEmitter,
  buildUser,
  onStartDiscovery,
  onStopDiscovery,
};
