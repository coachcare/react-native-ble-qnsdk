
# react-native-ble-qnsdk

A React Native module for interacting with the QNSDK for Bluetooth communication with health devices.

## Installation

Install the package using npm or yarn:

```bash
yarn add https://github.com/coachcare/react-native-ble-qnsdk.git
```

## Usage
To scan for a scale, you need to build the user first and then initiate the scan.


```
import { buildYolandaUser, startYolandaScan } from "react-native-ble-qnsdk";

const startScan = async () => {
  await requestBluetoothPermissions();

  await buildYolandaUser(userInfo);
  await startYolandaScan();
};
```

Make sure to define your `userInfo` as follows 

```
const userInfo = {
  birthday: "YYYY-MM-DD",
  gender: "male", // "male" or "female"
  id: "user_id",
  height: 175, // Height in cm
  unit: 1, // Measurement unit (1 for metric, 2 for imperial)
  athleteType: 0, // Athlete type (0 for general, 1 for athlete)
};
```

After scanning, you can use the useYolandaDeviceListener hook to listen for responses from the scale:

```
import { useYolandaDeviceListener } from "react-native-ble-qnsdk";

function MyComponent() {
  const setStatus = ...; // Define your state setter
  const setMeasurement = ...; // Define your state setter
  const setDevice = ...; // Define your state setter
  const addLog = ...; // Define your log function

  useYolandaDeviceListener({
    setStatus,
    setMeasurement,
    setDevice,
    addLog,
  });

  // ... rest of your component code ...
}
```

Sample hook for the scale listeners

```
import {

  IScaleStateEnum,
  IYolandaConnectionState
} from "@mobile/types/bluetooth.types"
import {
  ConnectionStatus,
  FinalMeasurementResponse,
  QNSDKEmitter,
  TemporaryMeasurementResponse,
  YolandaDeviceInfo,
  YolandaEventEmitter,
  YolandaEventTypeEnum
} from "react-native-ble-qnsdk"
import { useEffect, useRef } from "react"

export function useYolandaDeviceListener({ setDevice, setStatus, setMeasurement, addLog }): void {

  const filterStateChange = (value: number): void => {

    switch (value) {
      case IYolandaConnectionState.QNScaleStateMeasureCompleted:
        isSyncComplete.current = true
        return setStatus(IScaleStateEnum.COMPLETE)
      case IYolandaConnectionState.QNScaleStateDisconnected:
        isSyncComplete.current = false
        return setStatus(IScaleStateEnum.DISCONNECTED)
      case IYolandaConnectionState.QNScaleStateLinkLoss:
        isSyncComplete.current = false
        return setStatus(IScaleStateEnum.DISCONNECTED)
      case IYolandaConnectionState.QNScaleStateConnecting:
      case IYolandaConnectionState.QNScaleStateConnected:
        isSyncComplete.current = false
    }
  }


  const temporaryMeasurementResponse = (value: TemporaryMeasurementResponse): void => {
    setMeasurement(value)
  }

  const finalMeasurementResponse = (value: FinalMeasurementResponse): void => {
    setMeasurement(value)
  }

  const setDeviceInfo = (value: YolandaDeviceInfo): void => {
    setDevice(value)
  }

  const notificationFilter = async (response: YolandaEventEmitter) => {
    if (response.type === YolandaEventTypeEnum.SCALE_STATE_CHANGE) {
      filterStateChange(response.value)
    }
    if (response.type === YolandaEventTypeEnum.TEMPORARY_MEASUREMENT_EVENT) {
      temporaryMeasurementResponse(response.value)
    }
    if (response.type === YolandaEventTypeEnum.FINAL_MEASUREMENT_EVENT) {
      finalMeasurementResponse(response.value)
    }
    if (response.type === YolandaEventTypeEnum.DEVICE_INFO) {
      setDeviceInfo(response.value)
    }
  }

  useEffect(() => {
    const progressSubscription = QNSDKEmitter.addListener("uploadProgress", notificationFilter)
    return () => {
      progressSubscription?.remove()
    }
  }, [])
}
````

Relavent scale state enum

```
export enum IYolandaConnectionState {
  QNScaleStateDisconnected = 0,
  QNScaleStateLinkLoss = -1,
  QNScaleStateConnected = 1,
  QNScaleStateConnecting = 2,
  QNScaleStateDisconnecting = 3,
  QNScaleStateStartMeasure = 4,
  QNScaleStateRealTime = 5,
  QNScaleStateBodyFat = 7,
  QNScaleStateHeartRate = 8,
  QNScaleStateMeasureCompleted = 9,
  QNScaleStateWiFiBleStartNetwork = 10,
  QNScaleStateWiFiBleNetworkSuccess = 11,
  QNScaleStateWiFiBleNetworkFail = 12,
  QNScaleStateBleKitchenPeeled = 13,
  QNScaleStateHeightScaleMeasureFail = 14
}
```

The SDK will handle all of the interactions. Just listen for the final measurement response and handle the UI accordingly

## Additional Information
- You will have to add your SDK key to the project. If it doesn't use the same key id then you will have to fork the project and rename the key in the initSDK functions
-- Android - Add the key to Put it in the assets directory and use it during initialization:" file:///android_asset/filename.qn "to pass the file path
-- iOS - Be sure to add key to "Copy Bundle Resources" to have access to the key in the npm module. In Xcode Scroll down to the "Build Phases" tab and locate the "Copy Bundle Resources" phase. Add files by dragging them into the "Copy Bundle Resources" list or using the "+" button, and ensure the desired resources are included in your app bundle.
- Be sure to request proper bluetooth permissions before using this SDK. Permissions are handled outside of this logic

## License
This project is licensed under the MIT License

   
