//
//  BleQNSDKManager.swift
//  react-native-ble-qnsdk
//
//  Created by Jeffrey Drakos on 4/10/20.
//

import Foundation
import CoreBluetooth
import PromiseKit

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
        bleApi.initSdk("123456789", firstDataFile: file, callback: { error in })
        
        bleApi.discoveryListener = self
        bleApi.connectionChangeListener = self
        bleApi.dataListener = self
    }
    
    @objc(buildUser:birthday:height:gender:id:unit:)
    func buildUser(name: String, birthday: String, height: Int, gender: String, id: String, unit: Int) {
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
        
        let config = bleApi.getConfig()
        config?.unit = QNUnit(rawValue: UInt(unit))!
    }
    
    @objc(onStartDiscovery)
    func onStartDiscovery() {
        bleApi.startBleDeviceDiscovery({ error in
            // This callback indicates whether the startup scan method is successful
            if((error) != nil) {
                do {
                    if let error = error {
                        print("Jeff: failed to start the scan method, reason: \(error)")
                        
                    }
                }
            }
        })

    }
    
    @objc(onStopDiscovery)
    func onStopDiscovery() {
        print("Jeff: onStopDiscovery")
        bleApi.stopBleDeviceDiscorvery({ error in
            // This callback indicates whether the startup scan method is successful
            if((error) != nil) {
                do {
                    if let error = error {
                        print("Jeff: failed to stop the scan method, reason: \(error)")
                    }
                }
            } else {
                print("Jeff: SDK: stopBleDeviceDiscorvery success")
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
                        print("Jeff: failed to stop the scan method, reason: \(error)")
                    }
                }
            } else {
                print("Jeff: SDK: stopBleDeviceDiscorvery success")
            }
        })
        
    }
    
    
}

extension QNSDKManager: QNBleDeviceDiscoveryListener {
    public func onStartScan() {
        print("jeffd: on start scan")
    }
    
    public func onDeviceDiscover(_ device: QNBleDevice!) {
        print("jeffd: onDeviceDiscover", device)
        self.device = device
        self.onTryConnect()
    }
    
}

extension QNSDKManager: QNBleConnectionChangeListener {
    public func onConnecting(_ device: QNBleDevice!) {
        print("jeff: onConnecting", device)
    }
    
    public func onConnected(_ device: QNBleDevice!) {
        print("jeff: onConnected", device)
    }
    
    public func onServiceSearchComplete(_ device: QNBleDevice!) {
        print("jeff: onServiceSearchComplete", device)
    }
    
    public func onDisconnecting(_ device: QNBleDevice!) {
        print("jeff: onDisconnecting", device)
    }
    
    public func onConnectError(_ device: QNBleDevice!, error: Error!) {
        print("jeff: onConnectError device", device)
        print("jeff: onConnectError error", error)
    }
    
}

extension QNSDKManager: QNScaleDataListener {
    public func onGetUnsteadyWeight(_ device: QNBleDevice!, weight: Double) {
        print("jeff onGetUnsteadyWeight", weight)
        let jsonObject: [String: Any] = [
            "status": "sync",
            "weight": (weight * 1000)
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
                response["weight"] = (item.value * 1000)
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
        print("jeff onGetElectric", electric)
    }
    
    public func onScaleStateChange(_ device: QNBleDevice!, scaleState state: QNScaleState) {
        print("jeff onScaleStateChange", state)
    }
    
    public func onScaleEventChange(_ device: QNBleDevice!, scaleEvent: QNScaleEvent) {
        print("jeff onScaleEventChange", scaleEvent)
    }
    
    
}

