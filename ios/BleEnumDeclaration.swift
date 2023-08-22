//
//  BleEnumDeclaration.swift
//  react-native-ble-qnsdk
//
//  Created by Jeffrey Drakos on 8/22/23.
//

import Foundation

enum EventEmitterState: String {
    case scaleEventChange = "scaleEventChange"
    case scaleStateChange = "scaleStateChange"
    case connectionStatus = "connectionStatus"
    case measurementReceived = "measurementReceived"
    case uploadProgress = "uploadProgress"
}
