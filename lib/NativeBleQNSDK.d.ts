import { TurboModule } from "react-native";
import type { EventEmitter } from 'react-native/Libraries/Types/CodegenTypes';
export interface Spec extends TurboModule {
    readonly onValueChanged: EventEmitter<number>;
}
declare const _default: Spec;
export default _default;
