import { NativeEventEmitter } from 'react-native';
declare const BleQnsdk: any;
declare const QnsSDKEmitter: NativeEventEmitter;
interface IYolandaUser {
    birthday: string;
    gender: 'male' | 'female';
    id: string;
    height: number;
    unit: number;
    athleteType: number;
}
declare function buildYolandaUser(user: IYolandaUser): Promise<number>;
declare function startYolandaScan(): Promise<void>;
declare function stopYolandaScan(): Promise<void>;
export { BleQnsdk, QnsSDKEmitter, buildYolandaUser, startYolandaScan, stopYolandaScan, };
