import type { TurboModule } from 'react-native';
import type { EventEmitter } from 'react-native/Libraries/Types/CodegenTypes';
export interface Spec extends TurboModule {
    buildUser(birthday: string, height: number, gender: string, id: string, unit: number, athleteType: number): Promise<void>;
    onStartDiscovery(): Promise<void>;
    onStopDiscovery(): Promise<void>;
    fetchConnectedDeviceInfo(): void;
    disconnectDevice(): Promise<void>;
    readonly onValueChanged: EventEmitter<number>;
}
declare const _default: Spec;
export default _default;
