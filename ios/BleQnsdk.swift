//
//  BleBleQnsdk.swift
//  react-native-ble-qnsdk
//
//  Created by Jeffrey Drakos on 4/10/20.
//

import React // for RCTEventEmitter
import Foundation
import CoreBluetooth

@objc(BleQnsdk)
public class BleQnsdk: RCTEventEmitter  {
    var bleApi: QNBleApi!
    var user: QNUser!
    var device: QNBleDevice!
    var scaleDataAry: [AnyHashable] = []
    
    override public func supportedEvents() -> [String]! {
        return ["uploadProgress"]
    }
    
    
    override init() {
        super.init()
        bleApi = QNBleApi.shared()
        let file = Bundle.main.path(forResource: "Lexington202208", ofType: "qn")
        bleApi.initSdk("Lexington202004", firstDataFile: file, callback: { error in })
        
        bleApi.discoveryListener = self
        bleApi.connectionChangeListener = self
        bleApi.dataListener = self
    }
    

    @objc func buildUser(_ birthday: String, height: Int, gender: String, id: String, unit: Int, athleteType: Int, resolver resolve: @escaping RCTPromiseResolveBlock,
                         rejecter reject: @escaping RCTPromiseRejectBlock) {
        let birthdayDateFormatter = DateFormatter()
        birthdayDateFormatter.dateFormat = "yyyy/MM/dd"
        let date = birthdayDateFormatter.date(from: birthday)
        
        self.user = bleApi.buildUser(id, height: Int32(height), gender: gender, birthday: date, callback: { error in
            if (error != nil) {
                self.sendEvent(withName: "uploadProgress", body: [
                    "connectionStatus": [
                        "status": "error",
                        "error": error,
                        "description": "Build user error"
                    ]
                ])
            }
            
        })
        
        self.user.athleteType = athleteType == 1 ? YLAthleteType.sport :YLAthleteType.default
        let config = bleApi.getConfig()
        
        config?.unit = unit == 0 ? QNUnit.KG : QNUnit.LB
        resolve(true)
    }
    
    // @objc
    // func onStartDiscovery(_ resolve: RCTPromiseResolveBlock,
    //                       rejecter reject: RCTPromiseRejectBlock) {
    //     bleApi.startBleDeviceDiscovery({ error in
    //         // This callback indicates whether the startup scan method is successful
    //         if((error) != nil) {
    //             do {
    //                 if let error = error {
    //                     print("Failed to start the scan method, reason: \(error)")
    //                     self.sendEvent(withName: "uploadProgress", body: [
    //                         "connectionStatus": [
    //                             "status": "error",
    //                             "error": error,
    //                             "description": "Start Ble Discovery Error"
    //                         ]
    //                     ])
                        
    //                 }
    //             }
    //         }
    //     })
    //     resolve(true)
        
    // }

    @objc
    func onStartDiscovery(_ resolve: @escaping RCTPromiseResolveBlock,
                        rejecter reject: @escaping RCTPromiseRejectBlock) {
        bleApi.startBleDeviceDiscovery { error in
            if let error = error {
                let errorMessage = "Failed to start the scan method, reason: \(error)"
                let errorCode = "BLE_DISCOVERY_ERROR"
                reject(errorCode, errorMessage, error)
                return
            }
            
            resolve(true)
        }
    }

    @objc
    func onStopDiscovery(_ resolve: @escaping RCTPromiseResolveBlock,
                        rejecter reject: @escaping RCTPromiseRejectBlock) {
        bleApi.stopBleDeviceDiscorvery { error in
            if let error = error {
                let errorMessage = "Failed to stop the scan method, reason: \(error)"
                let errorCode = "BLE_STOP_DISCOVERY_ERROR"
                reject(errorCode, errorMessage, error)
                return
            }
            
            resolve(true)
        }
    }

    // @objc(onStopDiscovery)
    // func onStopDiscovery() {
    //     bleApi.stopBleDeviceDiscorvery({ error in
    //         // This callback indicates whether the startup scan method is successful
    //         if((error) != nil) {
    //             do {
    //                 if let error = error {
    //                     print("Failed to stop the scan method, reason: \(error)")
    //                 }
    //             }
    //         }
    //     })
    // }
    
    func onTryConnect() {
        self.onStopDiscovery()
        bleApi.connect(self.device, user: self.user, callback: { error in
            // This callback indicates whether the startup scan method is successful
            if((error) != nil) {
                do {
                    if let error = error {
                        print("Failed to connect, reason: \(error)")
                        self.sendEvent(withName: "uploadProgress", body: [
                            "connectionStatus": [
                                "status": "error",
                                "error": error,
                                "description": "Connection Error"
                            ]
                        ])
                    }
                }
            }
        })
        
    }
    
    func convertPoundsToGrams(_ weight: Double) -> Double {
        var finalWeight = weight * 1000
        
        // In order to have the same value for lb's in the app, convert from lb's to grams
        if bleApi.getConfig().unit == QNUnit.LB {
            let pounds = bleApi.convertWeight(withTargetUnit: weight, unit: QNUnit.LB)
            let convertedWeight = 453.59237 * pounds
            finalWeight = convertedWeight
        }
        
        return finalWeight
    }
    
}

extension BleQnsdk: QNBleDeviceDiscoveryListener {
    public func onStartScan() {
        print("On start scan")
    }
    
    public func onDeviceDiscover(_ device: QNBleDevice!) {
        self.device = device
        self.onTryConnect()
    }
    
}

extension BleQnsdk: QNBleConnectionChangeListener {
    public func onDisconnected(_ device: QNBleDevice!) {
        self.sendEvent(withName: "uploadProgress", body: [
            "connectionStatus": [
                "status": "onDisconnected"
            ]
        ])
        
    }
    public func onConnecting(_ device: QNBleDevice!) {
        self.sendEvent(withName: "uploadProgress", body: [
            "connectionStatus": [
                "status": "onConnecting",
                "deviceId": device.modeId
            ]
        ])
        
        
        
    }
    
    public func onConnected(_ device: QNBleDevice!) {
        self.sendEvent(withName: "uploadProgress", body: [
            "connectionStatus": [
                "status": "onConnected",
                "deviceId": device.modeId
            ]
        ])
    }
    
    public func onServiceSearchComplete(_ device: QNBleDevice!) {
        print("onServiceSearchComplete")
        self.sendEvent(withName: "uploadProgress", body: [
            "connectionStatus": [
                "status": "onServiceSearchComplete"
            ]
        ])
    }
    
    public func onDisconnecting(_ device: QNBleDevice!) {
        print("onDisconnecting")
        self.sendEvent(withName: "uploadProgress", body: [
            "connectionStatus": [
                "status": "onDisconnecting"
            ]
        ])
    }
    
    public func onConnectError(_ device: QNBleDevice!, error: Error!) {
        self.sendEvent(withName: "uploadProgress", body: [
            "connectionStatus": [
                "status": "error",
                "error": error
            ]
        ])
    }
    
}

extension BleQnsdk: QNScaleDataListener {
    public func onGetUnsteadyWeight(_ device: QNBleDevice!, weight: Double) {
//        var finalWeight = weight * 1000
//
        let finalWeight = convertPoundsToGrams(weight)
//        // In order to have the same value for lb's in the app we must convert from lb's to grams
//        if (bleApi.getConfig().unit == QNUnit.LB) {
//            let pounds = bleApi.convertWeight(withTargetUnit: weight, unit: QNUnit.LB)
//            let convertedWeight = 453.59237 * pounds
//            finalWeight = convertedWeight
//        }
        
        self.sendEvent(withName: "uploadProgress", body: [
            "measurement": [
                "scaleId": device.modeId,
                "weight": finalWeight
            ]
        ])
    }
    
    public func filterResponse(_ scaleData: [QNScaleItemData]) -> [String:Any]? {
        var response = [String:Any]()
        for item in scaleData {
            
            if (item.name == "BMR") {
                response["basalMetabolicRate"] = item.value
            }
            if (item.name == "visceral fat") {
                response["visceralFatTanita"] = item.value
            }
            if (item.name == "weight") {
                response["weight"] = convertPoundsToGrams(item.value)
            }
            if (item.name == "lean body weight") {
                response["fatFreeMass"] = (item.value * 1000)
            }
            if (item.name == "body fat rate") {
                response["bodyFat"] = (item.value * 1000)
            }
            if (item.name == "body water rate") {
                response["waterPercentage"] = (item.value * 1000)
            }
            if (item.name == "muscle rate") {
                response["skeletalMuscleRatio"] = (item.value * 1000)
            }
            if (item.name == "muscle mass") {
                response["muscleMass"] = (item.value * 1000)
            }
            
        }
        
        return response
    }
    
    public func onGetScaleData(_ device: QNBleDevice!, data scaleData: QNScaleData!) {
        let data = self.filterResponse(scaleData.getAllItem())
        
        self.sendEvent(withName: "uploadProgress", body: [
            "measurement":
                data
        ])
    }
    
    public func onGetStoredScale(_ device: QNBleDevice!, data storedDataList: [QNScaleStoreData]!) {
        print("onGetStoredScale")
        print(storedDataList)
    }
    
    public func onGetElectric(_ electric: UInt, device: QNBleDevice!) {
        print("onGetElectric", electric)
    }
    
    public func onScaleStateChange(_ device: QNBleDevice!, scaleState state: QNScaleState) {
        self.sendEvent(withName: "uploadProgress", body: [
            "scaleStateChange": state.rawValue
        ])
        
    }
    
    public func onScaleEventChange(_ device: QNBleDevice!, scaleEvent: QNScaleEvent) {
        self.sendEvent(withName: "uploadProgress", body: [
            "scaleEventChange": [
                "value": scaleEvent
            ]
        ])
    }
    
}

