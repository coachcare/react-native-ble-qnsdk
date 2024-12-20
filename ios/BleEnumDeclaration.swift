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
    case logging = "logging"
    case temporaryMeasurementReceived = "temporaryMeasurementReceived"
    case finalMeasurementReceived = "finalMeasurementReceived"
    case uploadProgress = "uploadProgress"
}

enum LoggingState: String {
    case deviceDiscovered = "deviceDiscovered"
    case startScan = "startScan"
    case onGetStoredScale = "onGetStoredScale"
    case onGetElectric = "onGetElectric"
}

enum ConnectionStatusFields: String {
    case status = "status"
    case error = "error"
    case description = "description"
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
