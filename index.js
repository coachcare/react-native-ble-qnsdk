// import { NativeEventEmitter, NativeModules } from 'react-native';

// var BleQnsdk = NativeModules.QNSDKManager;

// class BleQnsdkManager {
//     birthday
//     gender
//     id
//     height
//     unit
//     athleteType
//     QnsSDKEmitter = new NativeEventEmitter(BleQnsdk)

//     buildUser(user) {
//         return new Promise((fulfill, reject) => {
//             this.birthday = user && user.birthday || "1986/10/09"
//             this.gender = user && user.gender || "female"
//             this.id = user && user.id || "1"
//             this.height = user && user.height || 85
//             this.unit = user.unit !== undefined ? user.unit : this.unit
//             this.athleteType = user.athleteType !== undefined ? user.athleteType : 0

//             BleQnsdk.buildUser("buildUser", this.birthday, this.height, this.gender, this.id, this.unit, this.athleteType)
//                 .then(() => fulfill())
//                 .catch(() => reject())
//         });
//     }

//     scan() {
//         return new Promise((fulfill, reject) => {
//             BleQnsdk.onStartDiscovery("onStartDiscovery")
//                 .then(() => fulfill())
//                 .catch(() => reject())
//         });
//     }

//     stopScan() {
//         BleQnsdk.onStopDiscovery()
//     }


// }

// module.exports = new BleQnsdkManager();

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
function buildYolandaUser(user) {
  return BleQnsdk.buildUser(
    user.birthday,
    user.height,
    user.gender,
    user.id,
    user.unit,
    user.athleteType
  );
}
function startYolandaScan() {
  return BleQnsdk.onStartDiscovery();
}
function stopYolandaScan() {
  return BleQnsdk.onStopDiscovery();
}
export {
  BleQnsdk,
  QnsSDKEmitter,
  buildYolandaUser,
  startYolandaScan,
  stopYolandaScan,
};
