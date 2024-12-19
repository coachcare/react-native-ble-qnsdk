import { TurboModule, TurboModuleRegistry } from "react-native";

export interface Spec extends TurboModule {
  readonly buildUser: (
    birthday: string,
    height: number,
    gender: string,
    id: string,
    unit: number,
    athleteType: number
  ) => Promise<void>;
  readonly onStartDiscovery: () => Promise<void>;
  readonly onStopDiscovery: () => Promise<void>;
  readonly fetchConnectedDeviceInfo: () => void;
  readonly disconnectDevice: () => Promise<void>;
  readonly onValueChanged: () => Promise<void>;
}

export default TurboModuleRegistry.get<Spec>("BleQNSDK") as Spec | null;

