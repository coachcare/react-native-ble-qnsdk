import { TurboModule, TurboModuleRegistry } from "react-native";
import type {

  Int32,
} from "react-native/Libraries/Types/CodegenTypes";

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
  readonly onValueChanged: (value: Int32) => void;
}

// Exporting the TurboModule instance
export default TurboModuleRegistry.getEnforcing<Spec>("BleQNSDK");
