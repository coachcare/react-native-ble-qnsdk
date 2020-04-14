import { NativeEventEmitter, NativeModules } from 'react-native';

var BleQnsdk = NativeModules.QNSDKManager;

class BleQnsdkManager {
    birthday
    gender
    id
    height
    QnsSDKEmitter = new NativeEventEmitter(BleQnsdk)

    buildUser(user) {
        return new Promise((fulfill, reject) => {
            this.birthday = user && user.birthday || "1986/10/09"
            this.gender = user && user.gender || "female"
            this.id = user && user.id || "1"
            this.height = user && user.height || 85
            this.unit = user.unit !== undefined ? user.unit : this.unit

            BleQnsdk.buildUser("buildUser", this.birthday, this.height, this.gender, this.id, this.unit)
                .then(() => fulfill())
                .catch(() => reject())
        });
    }

    scan() {
        return new Promise((fulfill, reject) => {
            BleQnsdk.onStartDiscovery("onStartDiscovery")
                .then(() => fulfill())
                .catch(() => reject())
        });
    }

    stopScan() {
        BleQnsdk.onStopDiscovery()
    }


}

module.exports = new BleQnsdkManager();