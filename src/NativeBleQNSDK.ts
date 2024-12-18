import { TurboModule, TurboModuleRegistry } from "react-native";

export interface Spec extends TurboModule {
  // Promise-based methods
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

  // Event emitter for value changes
  readonly emitOnValueChanged: (value: number) => void;
}

// Exporting the TurboModule instance
export default TurboModuleRegistry.get<Spec>("BleQNSDK")  as Spec | null;
