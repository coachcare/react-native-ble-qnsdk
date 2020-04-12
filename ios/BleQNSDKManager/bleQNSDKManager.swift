//
//  BleQNSDKManager.swift
//  react-native-ble-qnsdk
//
//  Created by Jeffrey Drakos on 4/10/20.
//

import Foundation
import CoreBluetooth

@objc(QNSDKManager)
public class QNSDKManager : NSObject, QNLogProtocol {
    var bleApi: QNBleApi!
//    var q: QNSDKConnectionDelegate!
//    weak var delegate : QNSDKConnectionDelegate? = nil
    var centralManager: CBCentralManager!
    var user: QNUser!
    var device: QNBleDevice!
    var scaleDataAry: [AnyHashable] = []
//    let state = PublishSubject<String>()
//
//    let observable : Observable<String>
//    private let QNDeviceState: ReplaySubject<String> = "initializing"
//
//    override var rating: Int {
//        get { return super.rating }
//        set {
//            super.rating = newValue
//            ratingSubject.on(.next(super.rating))
//        }
//    }
    public func onLog(_ log: String) {
        //print("log", log)
    }
    
    override init() {
        super.init()
        bleApi = QNBleApi.shared()
        let file = Bundle.main.path(forResource: "123456789", ofType: "qn")
        bleApi.initSdk("123456789", firstDataFile: file, callback: { error in })
        self.buildUser()
        
        bleApi.discoveryListener = self
        bleApi.logListener = self
        bleApi.connectionChangeListener = self
        bleApi.dataListener = self
    }
    
    func buildUser() {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy/MM/dd HH:mm"
        let someDateTime = formatter.date(from: "1986/10/08 22:31")
        
        self.user = bleApi.buildUser("1", height: 85, gender: "male", birthday: someDateTime, callback: { error in
            if (error != nil) {
                print("error building user", error)
            } else {
                print("No error building user")
            }
            
        })
    }
    
    func onStartDiscovery() {
        print("Jeff: onStartDiscovery")
        scaleDataAry = []
        bleApi.startBleDeviceDiscovery({ error in
            // This callback indicates whether the startup scan method is successful
            if((error) != nil) {
                do {
                    if let error = error {
                        print("Jeff: failed to start the scan method, reason: \(error)")
                    }
                }
            } else {
                print("Jeff: SDK: startBleDeviceDiscovery success")
            }
        })
    }
    
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
        print("jeff: onTryConnect")
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
        print("jeff: onConnectError", device)
    }
    
}

extension QNSDKManager: QNScaleDataListener {
    public func onGetUnsteadyWeight(_ device: QNBleDevice!, weight: Double) {
        print("jeff onGetUnsteadyWeight", weight)
    }
    
    public func onGetScaleData(_ device: QNBleDevice!, data scaleData: QNScaleData!) {
        print("jeff onGetScaleData", scaleData)
        
        
        for item in scaleData.getAllItem() {
            print("item.name")
            print(item.name)
            print("item.value")
            print(item.value)
            self.scaleDataAry.append(item)
        }
        
        print("self.scaleDataAry[0")
        print(self.scaleDataAry[0])
    }
    
    public func onGetStoredScale(_ device: QNBleDevice!, data storedDataList: [QNScaleStoreData]!) {
        print("jeff onGetStoredScale", storedDataList)
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

//extension QNSDKManager : QNSDKConnectionDelegate {
//    public func onQNDeviceFound() {
//        print("ONNNNN onQNDeviceFound")
//    }
//
//
//}
