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

function buildYolandaUser(user: IYolandaUser): Promise<number> {
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

export {
  BleQnsdk,
  QnsSDKEmitter,
  buildYolandaUser,
  startYolandaScan,
  stopYolandaScan,
};
