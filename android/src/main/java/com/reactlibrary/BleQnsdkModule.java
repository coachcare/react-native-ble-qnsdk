package com.coachcare;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.pm.PackageManager;
import android.text.format.DateUtils;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.uimanager.IllegalViewOperationException;
import com.qingniu.qnble.utils.QNLogUtils;
import com.yolanda.health.qnblesdk.listener.QNBleConnectionChangeListener;
import com.yolanda.health.qnblesdk.listener.QNDataListener;
import com.yolanda.health.qnblesdk.listener.QNResultCallback;
import com.yolanda.health.qnblesdk.out.QNBleApi;
import com.yolanda.health.qnblesdk.constant.CheckStatus;
import com.yolanda.health.qnblesdk.constant.QNIndicator;
import com.yolanda.health.qnblesdk.constant.UserGoal;
import com.yolanda.health.qnblesdk.constant.UserShape;
import com.yolanda.health.qnblesdk.listener.QNBleDeviceDiscoveryListener;
import com.yolanda.health.qnblesdk.listener.QNResultCallback;
import com.yolanda.health.qnblesdk.out.QNBleApi;
import com.yolanda.health.qnblesdk.out.QNBleBroadcastDevice;
import com.yolanda.health.qnblesdk.out.QNBleDevice;
import com.yolanda.health.qnblesdk.out.QNConfig;
import com.yolanda.health.qnblesdk.out.QNScaleData;
import com.yolanda.health.qnblesdk.out.QNScaleStoreData;
import com.yolanda.health.qnblesdk.out.QNShareData;
import com.yolanda.health.qnblesdk.out.QNUser;
import com.yolanda.health.qnblesdk.out.QNUtils;
import com.yolanda.health.qnblesdk.out.QNWiFiConfig;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class BleQnsdkModule extends ReactContextBaseJavaModule implements LifecycleEventListener {

    private final ReactApplicationContext reactContext;
    private QNBleApi mQNBleApi;
  private boolean loaded = false;
  public static final String FORMAT_SHORT = "yyyy-MM-dd";


    public BleQnsdkModule(ReactApplicationContext reactContext) {

        super(reactContext);
        Log.w("Jeff", "on init");

        reactContext.addLifecycleEventListener(this);
        this.reactContext = reactContext;
//        this.initialize();
    }

  public void initialize() {

//    QNLogUtils.setLogEnable(BuildConfig.DEBUG);
    if (this.loaded == false) {
      final ReactApplicationContext context = getReactApplicationContext();
      mQNBleApi = QNBleApi.getInstance(context);
      this.setConfig();

      this.initSDK();
      this.setDiscoveryListener();
      this.setConnectionListener();
      this.setDataListener();
    }

    this.loaded = true;
    Log.w("Jeff", "on initialize");
    Log.w("Jeff", "on initialize loaded" + this.loaded);
  }

  public void setConfig() {
    QNConfig mQnConfig = mQNBleApi.getConfig();
    mQnConfig.setNotCheckGPS(true);
    mQnConfig.setAllowDuplicates(false);
    mQnConfig.setDuration(0);
//    mQnConfig.setScanOutTime(mConfig.getScanOutTime());
//    mQnConfig.setConnectOutTime(mConfig.getConnectOutTime());
//    mQnConfig.setUnit(mConfig.getUnit());
    mQnConfig.setOnlyScreenOn(false);
    Log.w("Jeff", "on setNotCheckGPS");
//    mQnConfig.setOnlyScreenOn(false);
//    mQnConfig.setScanOutTime(mConfig.getScanOutTime());
//    mQnConfig.setConnectOutTime(mConfig.getConnectOutTime());
//    mQnConfig.setUnit(mConfig.getUnit());
//    mQnConfig.setOnlyScreenOn(mConfig.isOnlyScreenOn());
    //设置扫描对象
//    mQnConfig.save(new QNResultCallback() {
//      @Override
//      public void onResult(int i, String s) {
//        Log.d("ScanActivity", "initData:" + s);
//      }
//    });
    mQnConfig.save(new QNResultCallback() {
      @Override
      public void onResult(int i, String s) {
        Log.d("ScanActivity", "initData:" + s);
      }
    });
  }

  public void initSDK() {
//    System.out.print ("Jeff: on create");
    Log.w("Jeff", "on initSDK");
    String encryptPath = "file:///android_asset/123456789.qn";
//    QNBleApi mQNBleApi = QNBleApi.getInstance(this.reactContext);
    mQNBleApi.initSdk("123456789", encryptPath, new QNResultCallback() {
      @Override
      public void onResult(int code, String msg) {
        Log.d("BaseApplication", "Initialization file\n" + msg);
      }
    });
  }

  @Override
  public void onHostResume() {
    // Activity `onResume`
    Log.w("Jeff", "on onHostResume");
    this.initialize();
  }

  @Override
  public void onHostPause() {
    Log.w("Jeff", "on onHostPause");
  }

  @Override
  public void onHostDestroy() {
    Log.w("Jeff", "on onHostDestroy");
  }


    @Override
    public String getName() {
        return "QNSDKManager";
    }

//    @ReactMethod
//    public void buildUser(String name, int height, String gender, String id, int unit, String athleteType, Callback successCallback, Callback errorCallback) {
//      System.out.print ("Jeff: buildUser");
//        // TODO: Implement some actually useful functionality
//     //   callback.invoke("Received numberArgument: " + numberArgument + " stringArgument: " + stringArgument);
//        try {
//          //  "buildUser", this.birthday, this.height, this.gender, this.id, this.unit, this.athleteType
//          successCallback.invoke("build user success");
//          } catch (IllegalViewOperationException e) {
//          errorCallback.invoke("build user reject", e);
//          }
//    }

  @ReactMethod
  public void buildUser(String name, String birthday, int height, String gender, String id, int unit, int athleteType, Promise promise) {
//    System.out.print ("Jeff: buildUser");
    Log.w("Jeff", "buildUser");

    try {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_SHORT); // here set the pattern as you date in string was containing like date/month/year
        Date formattedBirthday = sdf.parse(birthday);
      mQNBleApi.buildUser(id,
        height, gender, formattedBirthday, athleteType, new QNResultCallback() {
          @Override
          public void onResult(int code, String msg) {
            Log.w("Jeff Build User", "Build User message:" + msg);
          }
        });

//      this.setConfig();
      Log.w("Jeff", "no error b");
      //  "buildUser", this.birthday, this.height, this.gender, this.id, this.unit, this.athleteType
      promise.resolve("build user success");
    } catch (IllegalViewOperationException | ParseException e) {
      Log.w("Jeff", "error");
      promise.reject("build user reject", e);
    }
  }

//  public static String dateToString(Date date, String formatShort) {
//    return dateToString(date, FORMAT_SHORT);
//  }

  public static void verifyPermissions(Activity activity) {
    //判断是否已经赋予权限

    Log.w("Jeff", "verifyPermissionsss");
    if (ContextCompat.checkSelfPermission(activity,
      Manifest.permission.ACCESS_COARSE_LOCATION)
      != PackageManager.PERMISSION_GRANTED) {
      Log.w("Jeff", "ACCESS_COARSE_LOCATION 1");
      //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。

    }
    if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
      Manifest.permission.ACCESS_COARSE_LOCATION)) {
      Log.w("Jeff", "ACCESS_COARSE_LOCATION 2");
      //这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限
    }


    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
      Log.w("Jeff", "IN FIRST ACCESS_FINE_LOCATION");
      return;
    }else{
      Log.w("Jeff", "IN SECOND ACCESS_FINE_LOCATION");
      // Write you code here if permission already given.
    }

    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
      Log.w("Jeff", "IN FIRST ACCESS_COARSE_LOCATION");
      return;
    }else{
      Log.w("Jeff", "IN SECOND ACCESS_COARSE_LOCATION");
      // Write you code here if permission already given.
    }
  }

  public void setConnectionListener() {
    Log.w("Jeff", "setConnectionListener");
    final ReactApplicationContext context = getReactApplicationContext();
    mQNBleApi.setBleConnectionChangeListener(new QNBleConnectionChangeListener() {
      //connecting
      @Override
      public void onConnecting(QNBleDevice device) {
        Log.w("Jeff", "onConnecting");
//        setBleStatus(QNScaleStatus.STATE_CONNECTING);
      }
      //connected
      @Override
      public void onConnected(QNBleDevice device) {
        Log.w("Jeff", "onConnected");
//        setBleStatus(QNScaleStatus.STATE_CONNECTED);
      }

      @Override
      public void onServiceSearchComplete(QNBleDevice device) {
        Log.w("Jeff", "onServiceSearchComplete");
//The status is the method after the search service is completed, usually this method does not require business logic
      }

      //The connection is being disconnected. When the disconnection is called, it will be called back immediately.
      @Override
      public void onDisconnecting(QNBleDevice device) {
//        setBleStatus(QNScaleStatus.STATE_DISCONNECTING);
        Log.w("Jeff", "onDisconnecting");
      }

      //Disconnect, callback after disconnect
      @Override
      public void onDisconnected(QNBleDevice device) {
//        setBleStatus(QNScaleStatus.STATE_DISCONNECTED);
        Log.w("Jeff", "onDisconnected");
      }

      //There was a connection error, please refer to the attached table for the error code
      @Override
      public void onConnectError(QNBleDevice device, int errorCode) {
//        setBleStatus(QNScaleStatus.STATE_DISCONNECTED);
        Log.w("Jeff", "onConnectError");
      }

      @Override
      public void onScaleStateChange(QNBleDevice qnBleDevice, int i) {
        Log.w("Jeff", "onScaleStateChange");
      }
    });

  }

  public void setDataListener() {
    Log.w("Jeff", "setDataListener");
//    mQNBleApi.setDataListener(new QNDataListener() {
//      @Override
//      public void onGetUnsteadyWeight(QNBleDevice device, double weight) {
//        Log.w("Jeff", "onGetUnsteadyWeight");
//        Log.w("Jeff", String.valueOf(weight));
////This method receives unstable weight data. In one measurement, the method will call back multiple times until the data is stable and complete data is obtained.
//      }
//
//      @Override
//      public void onGetScaleData(QNBleDevice qnBleDevice, QNScaleData qnScaleData) {
//        Log.w("Jeff", "onGetScaleData");
//
//      }
//
//
//
//      @Override
//      public void onGetStoredScale(QNBleDevice device, List<QNScaleStoreData> storedDataList) {
////The method is to receive the storage data at the scale end. The processing method of the stored data can refer to the demo, or you can define it yourself
//      }
//
//      @Override
//      public void onGetElectric(QNBleDevice device, int electric) {
//
////This is the percentage of power obtained. Only the power obtained by the charging scale is meaningful.
//      }
//
//
//    });
//    final ReactApplicationContext context = getReactApplicationContext();
    mQNBleApi.setDataListener(new QNDataListener() {
      @Override
      public void onGetUnsteadyWeight(QNBleDevice device, double weight) {
        Log.w("Jeff", "onGetScaleData");
//This method receives unstable weight data. In one measurement, the method will call back multiple times until the data is stable and complete data is obtained.
      }

      @Override
      public void onGetScaleData(QNBleDevice device, QNScaleData data) {
//The method is to receive complete measurement data
      }

      @Override
      public void onGetStoredScale(QNBleDevice device, List<QNScaleStoreData> storedDataList) {
//The method is to receive the storage data at the scale end. The processing method of the stored data can refer to the demo, or you can define it yourself
      }

      @Override
      public void onGetElectric(QNBleDevice device, int electric) {
//This is the percentage of power obtained. Only the power obtained by the charging scale is meaningful.
      }


    });
  }


  public void setDiscoveryListener() {
    mQNBleApi.setBleDeviceDiscoveryListener(new QNBleDeviceDiscoveryListener() {
      @Override
      public void onDeviceDiscover(QNBleDevice device) {
        Log.w("Jeff", "onDeviceDiscoverr");
//        devices.add(device);
//        listAdapter.notifyDataSetChanged();
      }

      @Override
      public void onStartScan() {
        Log.w("Jeff", "onStartScan");
//        QNLogUtils.log("ScanActivity", "onStartScan");
//        isScanning = true;
      }

      @Override
      public void onStopScan() {
        Log.w("Jeff", "onStopScan");
//        QNLogUtils.log("ScanActivity", "onStopScan");
//        isScanning = false;
//        ToastMaker.show(ScanActivity.this, "已经停止扫描");
//        final ReactApplicationContext context = getReactApplicationContext();
        mQNBleApi.stopBleDeviceDiscovery(new QNResultCallback() {
          @Override
          public void onResult(int code, String msg) {
            QNLogUtils.log("ScanActivity", "onStopScan");
            if (code == CheckStatus.OK.getCode()) {
//              isScanning = false;
            }
          }
        });
      }

      @Override
      public void onScanFail(int code) {
        Log.w("Jeff", "onScanFail");
//        isScanning = false;
//        QNLogUtils.log("ScanActivity", "onScanFail:" + code);
//        Toast.makeText(ScanActivity.this, "扫描异常，请重启手机蓝牙!", Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onBroadcastDeviceDiscover(QNBleBroadcastDevice qnBleBroadcastDevice) {
        Log.w("Jeff", "onBroadcastDeviceDiscover");

      }
    });
  }

    @ReactMethod
    public void onStartDiscovery(String name, final Promise promise) {

      Log.w("scan", "code:");
      Activity activity = getCurrentActivity();

     // ActivityManager am = (ActivityManager)reactContext.getApplicationContext().getSystemService(Activity.ACTIVITY_SERVICE);
      verifyPermissions(activity);


      mQNBleApi.startBleDeviceDiscovery(new QNResultCallback() {
        @Override
        public void onResult(int code, String msg) {
          Log.w("ScanActivity", "code:" + code + ";msg:" + msg);
          if (code != CheckStatus.OK.getCode()) {
            promise.resolve("Success scan scan: ");
          }

        }
      });

    }

    @ReactMethod
    public void stopScan(Callback callback) {
        // TODO: Implement some actually useful functionality
        callback.invoke("stopScan: ");
    }
}
