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
    case deviceInfo = "deviceInfo"
    case measurementReceived = "measurementReceived"
    case uploadProgress = "uploadProgress"
}

enum ConnectionStatusFields: String {
    case status = "status"
    case error = "error"
    case description = "description"
    case measurementReceived = "measurementReceived"
    case uploadProgress = "uploadProgress"
}

enum ConnectionStatusState: String {
    case error = "error"
    case onDisconnected = "onDisconnected"
    case onConnecting = "onConnecting"
    case onConnected = "onConnected"
    case onServiceSearchComplete = "onServiceSearchComplete"
    case onDisconnecting = "onDisconnecting"
}

enum ConnectionStatusDescription: String {
    case connectionErrorDescription = "Connection Error"
    case buildUserErrorDescription = "Build user error"

}
