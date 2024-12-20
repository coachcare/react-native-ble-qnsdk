import { TurboModule, TurboModuleRegistry } from "react-native";
import type {EventEmitter} from 'react-native/Libraries/Types/CodegenTypes';


export interface Spec extends TurboModule {
    buildUser(
      birthday: string,
      height: number,
      gender: string,
      id: string,
      unit: number,
      athleteType: number
    ): Promise<void>;
    onStartDiscovery(): Promise<void>;
    onStopDiscovery(): Promise<void>;
    fetchConnectedDeviceInfo(): void;
    disconnectDevice(): void;
    readonly onValueChanged: EventEmitter<number>;
  }

export default TurboModuleRegistry.get<Spec>("BleQnsdk") as Spec | null;