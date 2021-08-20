//
//  BleQNSDKManager.swift
//  react-native-ble-qnsdk
//
//  Created by Jeffrey Drakos on 4/10/20.
//

import Foundation
import CoreBluetooth


@objc(QNSDKManager)
public class QNSDKManager : RCTEventEmitter {
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
        let file = Bundle.main.path(forResource: "123456789", ofType: "qn")
        bleApi.initSdk("Lexington202004", firstDataFile: file, callback: { error in })
        
        bleApi.discoveryListener = self
        bleApi.connectionChangeListener = self
        bleApi.dataListener = self
    }

    @objc(buildUser:birthday:height:gender:id:unit:athleteType:resolver:rejecter:)
    func buildUser(name: String, birthday: String, height: Int, gender: String, id: String, unit: Int, athleteType: Int, resolver resolve: RCTPromiseResolveBlock,
    rejecter reject: RCTPromiseRejectBlock) {
        let dateStr = birthday
        let dateFormat = DateFormatter()
        dateFormat.dateFormat = "yyyy/MM/dd"
        let date = dateFormat.date(from: dateStr)
        
        self.user = bleApi.buildUser(id, height: Int32(height), gender: gender, birthday: date, callback: { error in
            if (error != nil) {
                print("error building user", error)
            } else {
                print("No error building user")
            }
            
        })
        
        if (athleteType == 1) {
            self.user.athleteType = YLAthleteType.sport
        } else {
            self.user.athleteType = YLAthleteType.default
        }
        
        //self.user.athleteType = YLAthleteType(rawValue: YLAthleteType.RawValue(athleteType))
        let config = bleApi.getConfig()
        let scaleUnit: QNUnit
        
        if (unit == 0) {
            scaleUnit = QNUnit.KG
        } else {
            scaleUnit = QNUnit.LB
        }

        config?.unit = scaleUnit
        resolve(true)
    }
    
    @objc(onStartDiscovery:resolver:rejecter:)
    func onStartDiscovery(name:String, resolve: RCTPromiseResolveBlock,
    rejecter reject: RCTPromiseRejectBlock) {
        bleApi.startBleDeviceDiscovery({ error in
            // This callback indicates whether the startup scan method is successful
            if((error) != nil) {
                do {
                    if let error = error {
                        print("Failed to start the scan method, reason: \(error)")
                        
                    } 
                }
            }
        })
        resolve(true)
        
    }
    
    @objc(onStopDiscovery)
    func onStopDiscovery() {
        bleApi.stopBleDeviceDiscorvery({ error in
            // This callback indicates whether the startup scan method is successful
            if((error) != nil) {
                do {
                    if let error = error {
                        print("Failed to stop the scan method, reason: \(error)")
                    }
                }
            }
        })
    }
    
    func onTryConnect() {
        self.onStopDiscovery()
        bleApi.connect(self.device, user: self.user, callback: { error in
            // This callback indicates whether the startup scan method is successful
            if((error) != nil) {
                do {
                    if let error = error {
                        print("Failed to connect, reason: \(error)")
                    }
                }
            } 
        })
        
    }
    
    
}

extension QNSDKManager: QNBleDeviceDiscoveryListener {
    public func onStartScan() {
        print("On start scan")
    }
    
    public func onDeviceDiscover(_ device: QNBleDevice!) {
        self.device = device
        self.onTryConnect()
    }
    
}

extension QNSDKManager: QNBleConnectionChangeListener {
    public func onConnecting(_ device: QNBleDevice!) {
    }
    
    public func onConnected(_ device: QNBleDevice!) {
    }
    
    public func onServiceSearchComplete(_ device: QNBleDevice!) {
    }
    
    public func onDisconnecting(_ device: QNBleDevice!) {
    }
    
    public func onConnectError(_ device: QNBleDevice!, error: Error!) {
        let errorObject: [String: Any] = [
            "status": "error"
        ]
        
        self.sendEvent(withName: "uploadProgress", body: errorObject )
    }
    
}

extension QNSDKManager: QNScaleDataListener {
    public func onGetUnsteadyWeight(_ device: QNBleDevice!, weight: Double) {
        var finalWeight = weight * 1000
        
        // In order to have the same value for lb's in the app we must convert from lb's to grams
        if (bleApi.getConfig().unit == QNUnit.LB) {
            let pounds = bleApi.convertWeight(withTargetUnit: weight, unit: QNUnit.LB)
            let convertedWeight = 453.59237 * pounds
            finalWeight = convertedWeight
        }

        let jsonObject: [String: Any] = [
            "status": "sync",
            "weight": finalWeight
        ]
        
        self.sendEvent(withName: "uploadProgress", body: jsonObject )
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
                var finalWeight = item.value * 1000
                
                // In order to have the same value for lb's in the app we must convert from lb's to grams
                if (bleApi.getConfig().unit == QNUnit.LB) {
                    let pounds = bleApi.convertWeight(withTargetUnit: item.value, unit: QNUnit.LB)
                    let convertedWeight = 453.59237 * pounds
                    finalWeight = convertedWeight
                }
                
                response["weight"] = finalWeight
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
        var data = self.filterResponse(scaleData.getAllItem())
        data?["status"] = "complete"
        
        self.sendEvent(withName: "uploadProgress", body: data )
    }
    
    public func onGetStoredScale(_ device: QNBleDevice!, data storedDataList: [QNScaleStoreData]!) {
    }
    
    public func onGetElectric(_ electric: UInt, device: QNBleDevice!) {
    }
    
    public func onScaleStateChange(_ device: QNBleDevice!, scaleState state: QNScaleState) {
        if (state.rawValue == -1) {
            print("onScaleStateChange -- QNScaleStateLinkLoss")
            print("onScaleStateChange -- DISCONNECT???")
            let errorObject: [String: Any] = [
                "status": "disconnected"
            ]
        
            self.sendEvent(withName: "uploadProgress", body: errorObject )
        }
        else if (state.rawValue == 0){
            print("onScaleStateChange - QNScaleStateDisconnected")
        }else if (state.rawValue == 1){
            print("onScaleStateChange - QNScaleStateConnected")
            let statusObject: [String: Any] = [
                "status": "connected"
            ]
            
            self.sendEvent(withName: "uploadProgress", body: statusObject )
        }
        else if (state.rawValue == 2){
           print("onScaleStateChange - QNScaleStateConnecting")
        }
        else if (state.rawValue == 3){
            print("onScaleStateChange - QNScaleStateDisconnecting")
        }
        else if (state.rawValue == 4){
           print("onScaleStateChange - QNScaleStateStartMeasure")
        }
        else if (state.rawValue == 5){
           print("onScaleStateChange - QNScaleStateRealTime")
        }
        else if (state.rawValue == 9){
            print("onScaleStateChange - QNScaleStateMeasureCompleted")
        }
   
        

    }
    
    public func onScaleEventChange(_ device: QNBleDevice!, scaleEvent: QNScaleEvent) {
        print("OnScaleEventChange", scaleEvent)
    }
    
}

