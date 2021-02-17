package com.coachcare;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.uimanager.IllegalViewOperationException;

import com.coachcare.User;

import com.qingniu.qnble.utils.QNLogUtils;
//import com.yolanda.health.qnblesdk.constant.QNUnit;
//import com.yolanda.health.qnblesdk.listener.QNBleConnectionChangeListener;
//import com.yolanda.health.qnblesdk.listener.QNScaleDataListener;
//import com.yolanda.health.qnblesdk.listener.QNResultCallback;
//import com.yolanda.health.qnblesdk.out.QNBleApi;
//import com.yolanda.health.qnblesdk.constant.CheckStatus;
//import com.yolanda.health.qnblesdk.constant.QNIndicator;
//import com.yolanda.health.qnblesdk.constant.UserGoal;
//import com.yolanda.health.qnblesdk.constant.UserShape;
//import com.yolanda.health.qnblesdk.listener.QNBleDeviceDiscoveryListener;
//import com.yolanda.health.qnblesdk.listener.QNResultCallback;
//import com.yolanda.health.qnblesdk.out.QNBleApi;
//import com.yolanda.health.qnblesdk.out.QNBleBroadcastDevice;
//import com.yolanda.health.qnblesdk.out.QNBleDevice;
//import com.yolanda.health.qnblesdk.out.QNBleKitchenDevice;
//import com.yolanda.health.qnblesdk.out.QNConfig;
//import com.yolanda.health.qnblesdk.out.QNScaleData;
//import com.yolanda.health.qnblesdk.out.QNScaleItemData;
//import com.yolanda.health.qnblesdk.out.QNScaleStoreData;
//import com.yolanda.health.qnblesdk.out.QNShareData;
//import com.yolanda.health.qnblesdk.out.QNUser;
//import com.yolanda.health.qnblesdk.out.QNUtils;
//import com.yolanda.health.qnblesdk.out.QNWiFiConfig;
import com.qn.device.listener.QNScaleDataListener;
import com.qn.device.listener.QNResultCallback;
import com.qn.device.out.QNBleApi;
import com.qn.device.constant.CheckStatus;
import com.qn.device.constant.QNDeviceType;
import com.qn.device.constant.QNIndicator;
import com.qn.device.constant.UserGoal;
import com.qn.device.constant.UserShape;
import com.qn.device.listener.QNBleDeviceDiscoveryListener;
import com.qn.device.listener.QNResultCallback;
import com.qn.device.out.QNBleApi;
import com.qn.device.out.QNBleBroadcastDevice;
import com.qn.device.out.QNBleDevice;
import com.qn.device.out.QNBleKitchenDevice;
import com.qn.device.out.QNConfig;
import com.qn.device.out.QNScaleData;
import com.qn.device.out.QNScaleItemData;
import com.qn.device.out.QNScaleStoreData;
import com.qn.device.out.QNShareData;
import com.qn.device.out.QNUser;
import com.qn.device.out.QNUtils;
import com.qn.device.out.QNWiFiConfig;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.qn.device.listener.QNBleConnectionChangeListener;
import com.qn.device.constant.QNScaleStatus;


public class BleQnsdkModule extends ReactContextBaseJavaModule implements LifecycleEventListener {

  private final ReactApplicationContext reactContext;
  private QNBleApi mQNBleApi;
  public User mUser = new User();
  private boolean loaded = false;
  public static final String FORMAT_SHORT = "yyyy-MM-dd";


  public BleQnsdkModule(ReactApplicationContext reactContext) {

    super(reactContext);

    reactContext.addLifecycleEventListener(this);
    this.reactContext = reactContext;
//    this.initSDK();
  }

  public void initialize() {
    final ReactApplicationContext context = getReactApplicationContext();
    mQNBleApi = QNBleApi.getInstance(context);
    this.setConfig();

    this.initSDK();
    this.setDiscoveryListener();
    this.setConnectionListener();
    this.setDataListener();
  }

  public void setConfig() {
    QNConfig mQnConfig = mQNBleApi.getConfig();
    mQnConfig.setNotCheckGPS(true);
    mQnConfig.setAllowDuplicates(false);
    mQnConfig.setDuration(0);
    mQnConfig.setOnlyScreenOn(false);

    mQnConfig.save(new QNResultCallback() {
      @Override
      public void onResult(int i, String s) {
        Log.d("ScanActivity", "initData:" + s);
      }
    });
  }

  public void initSDK() {
    String encryptPath = "file:///android_asset/123456789.qn";
    mQNBleApi.initSdk("Lexington202004", encryptPath, new QNResultCallback() {
      @Override
      public void onResult(int code, String msg) {
        Log.d("BaseApplication", "Initialization file\n" + msg);
      }
    });

//    mQNBleApi.initSdk("123456789", encryptPath, new QNResultCallback() {
//      @Override
//      public void onResult(int code, String msg) {
//        Log.d("BaseApplication", "初始化文件" + msg);
//      }
//    });
  }

  @Override
  public void onHostResume() {
    this.initialize();
  }

  @Override
  public void onHostPause() {
    Log.w("Scale", "on onHostPause");
  }

  @Override
  public void onHostDestroy() {
    Log.w("S", "on onHostDestroy");
  }

  private QNUser createQNUser() {
    UserShape userShape;
    switch (mUser.getChoseShape()) {
      case 0:
        userShape = UserShape.SHAPE_NONE;
        break;
      case 1:
        userShape = UserShape.SHAPE_SLIM;
        break;
      case 2:
        userShape = UserShape.SHAPE_NORMAL;
        break;
      case 3:
        userShape = UserShape.SHAPE_STRONG;
        break;
      case 4:
        userShape = UserShape.SHAPE_PLIM;
        break;
      default:
        userShape = UserShape.SHAPE_NONE;
        break;
    }

    UserGoal userGoal;
    switch (mUser.getChoseGoal()) {
      case 0:
        userGoal = UserGoal.GOAL_NONE;
        break;
      case 1:
        userGoal = UserGoal.GOAL_LOSE_FAT;
        break;
      case 2:
        userGoal = UserGoal.GOAL_STAY_HEALTH;
        break;
      case 3:
        userGoal = UserGoal.GOAL_GAIN_MUSCLE;
        break;
      case 4:
        userGoal = UserGoal.POWER_OFTEN_EXERCISE;
        break;
      case 5:
        userGoal = UserGoal.POWER_LITTLE_EXERCISE;
        break;
      case 6:
        userGoal = UserGoal.POWER_OFTEN_RUN;
        break;
      default:
        userGoal = UserGoal.GOAL_NONE;
        break;
    }

    return mQNBleApi.buildUser(mUser.getUserId(),
      mUser.getHeight(), mUser.getGender(), mUser.getBirthDay(), mUser.getAthleteType(),
      userShape, userGoal, mUser.getClothesWeight(), new QNResultCallback() {
        @Override
        public void onResult(int code, String msg) {
          Log.d("ConnectActivity", "Response:" + msg);
        }
      });
  }

  @Override
  public String getName() {
    return "QNSDKManager";
  }

  @ReactMethod
  public void buildUser(String name, String birthday, int height, String gender, String id, int unit, int athleteType, Promise promise) {
    try {
      SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_SHORT); // here set the pattern as you date in string was containing like date/month/year
      Date formattedBirthday = sdf.parse(birthday);

      this.mUser.setAthleteType(athleteType);
      this.mUser.setBirthDay(formattedBirthday);
      this.mUser.setGender(gender);
      this.mUser.setHeight(height);
      this.mUser.setUserId(id);

      this.createQNUser();

      QNConfig mQnConfig = mQNBleApi.getConfig();
      mQnConfig.setUnit(unit);
      Log.d("BaseApplication", "buildUser file\n");
      promise.resolve("build user success");
    } catch (IllegalViewOperationException | ParseException e) {
      promise.reject("build user reject", e);
    }
  }

  public static void verifyPermissions(Activity activity) {
    if (ContextCompat.checkSelfPermission(activity,
      Manifest.permission.ACCESS_COARSE_LOCATION)
      != PackageManager.PERMISSION_GRANTED) {
    }
    if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
      Manifest.permission.ACCESS_COARSE_LOCATION)) {
    }


    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
      return;
    } else {
    }

    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
      return;
    } else {
    }
  }

  private void setBleStatus(int bleStatus) {
    String stateString;
    String btnString;
//    switch (bleStatus) {
//      case QNScaleStatus.STATE_CONNECTING: {
//        stateString = getResources().getString(R.string.connecting);
//        btnString = getResources().getString(R.string.disconnected);
//        mIsConnected = true;
//        break;
//      }
//      case QNScaleStatus.STATE_CONNECTED: {
//        stateString = getResources().getString(R.string.connected);
//        btnString = getResources().getString(R.string.disconnected);
//        mIsConnected = true;
//        break;
//      }
//      case QNScaleStatus.STATE_DISCONNECTING: {
//        stateString = getResources().getString(R.string.disconnect_in_progress);
//        btnString = getResources().getString(R.string.connect);
//        mIsConnected = false;
//
//        break;
//      }
//      case QNScaleStatus.STATE_LINK_LOSS: {
//        stateString = getResources().getString(R.string.connection_disconnected);
//        btnString = getResources().getString(R.string.connect);
//        mIsConnected = false;
//        break;
//      }
//      case QNScaleStatus.STATE_START_MEASURE: {
//        stateString = getResources().getString(R.string.measuring);
//        btnString = getResources().getString(R.string.disconnected);
//        break;
//      }
//      case QNScaleStatus.STATE_REAL_TIME: {
//        stateString = getResources().getString(R.string.real_time_weight_measurement);
//        btnString = getResources().getString(R.string.disconnected);
//        break;
//      }
//      case QNScaleStatus.STATE_BODYFAT: {
//        stateString = getResources().getString(R.string.impedance_measured);
//        btnString = getResources().getString(R.string.disconnected);
//        break;
//      }
//      case QNScaleStatus.STATE_HEART_RATE: {
//        stateString = getResources().getString(R.string.measuring_heart_rate);
//        btnString = getResources().getString(R.string.disconnected);
//        break;
//      }
//      case QNScaleStatus.STATE_MEASURE_COMPLETED: {
//        stateString = getResources().getString(R.string.measure_complete);
//        hmacTest.setText("");
//        btnString = getResources().getString(R.string.disconnected);
//        break;
//      }
//      case QNScaleStatus.STATE_WIFI_BLE_START_NETWORK:
//        stateString = getResources().getString(R.string.start_set_wifi);
//        btnString = getResources().getString(R.string.disconnected);
//        Log.d("ConnectActivity", "开始设置WiFi");
//        break;
//      case QNScaleStatus.STATE_WIFI_BLE_NETWORK_FAIL:
//        stateString = getResources().getString(R.string.failed_to_set_wifi);
//        btnString = getResources().getString(R.string.disconnected);
//        Log.d("ConnectActivity", "设置WiFi失败");
//        break;
//      case QNScaleStatus.STATE_WIFI_BLE_NETWORK_SUCCESS:
//        stateString = getResources().getString(R.string.success_to_set_wifi);
//        btnString = getResources().getString(R.string.disconnected);
//        Log.d("ConnectActivity", "设置WiFi成功");
//        break;
//      default: {
//        stateString = getResources().getString(R.string.connection_disconnected);
//        btnString = getResources().getString(R.string.connect);
//        mIsConnected = false;
//        break;
//      }
//    }

  }

  private void setConnectionListener() {
    mQNBleApi.setBleConnectionChangeListener(new QNBleConnectionChangeListener() {
      //正在连接
      @Override
      public void onConnecting(QNBleDevice device) {
        setBleStatus(QNScaleStatus.STATE_CONNECTING);
      }

      //已连接
      @Override
      public void onConnected(QNBleDevice device) {
        setBleStatus(QNScaleStatus.STATE_CONNECTED);
      }

      @Override
      public void onServiceSearchComplete(QNBleDevice device) {

      }

      //正在断开连接，调用断开连接时，会马上回调
      @Override
      public void onDisconnecting(QNBleDevice device) {
        setBleStatus(QNScaleStatus.STATE_DISCONNECTING);
      }

      // 断开连接，断开连接后回调
      @Override
      public void onDisconnected(QNBleDevice device) {
        WritableMap params = Arguments.createMap();
        params.putString("status", "disconnected");
        sendEventToJS("uploadProgress", params);
        setBleStatus(QNScaleStatus.STATE_DISCONNECTED);
      }

      //出现了连接错误，错误码参考附表
      @Override
      public void onConnectError(QNBleDevice device, int errorCode) {
        Log.d("ConnectActivity", "onConnectError:" + errorCode);
        WritableMap params = Arguments.createMap();
        params.putString("status", "error");
        sendEventToJS("uploadProgress", params);
        setBleStatus(QNScaleStatus.STATE_DISCONNECTED);
      }

    });
  }

//  public void setConnectionListener() {
//    final ReactApplicationContext context = getReactApplicationContext();
//    mQNBleApi.setBleConnectionChangeListener(new QNBleConnectionChangeListener() {
//      @Override
//      public void onConnecting(QNBleDevice device) {
//        Log.d("Drakos", "onConnecting ");
//      }
//
//      @Override
//      public void onConnected(QNBleDevice device) {
//        Log.d("Drakos", "onConnected ");
//      }
//
//      @Override
//      public void onServiceSearchComplete(QNBleDevice device) {
//      }
//
//      //The connection is being disconnected. When the disconnection is called, it will be called back immediately.
//      @Override
//      public void onDisconnecting(QNBleDevice device) {
//      }
//
//      //Disconnect, callback after disconnect
//      @Override
//      public void onDisconnected(QNBleDevice device) {
//      }
//
//      //There was a connection error, please refer to the attached table for the error code
//      @Override
//      public void onConnectError(QNBleDevice device, int errorCode) {
//        Log.d("Drakos", "onConnectError ");
//      }
//    });
//
//  };

  public void sendEventToJS(String eventName, @Nullable WritableMap params) {
    Log.d("Drakos", "sendEventToJS ");
    this.reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
  }

//  private void initUserData() {
//    mQNBleApi.setDataListener(new QNScaleDataListener() {
//      @Override
//      public void onGetUnsteadyWeight(QNBleDevice device, double weight) {
//        Log.d("ConnectActivity", "体重是:" + weight);
//        mWeightTv.setText(initWeight(weight));
//      }
//
//      @Override
//      public void onGetScaleData(QNBleDevice device, QNScaleData data) {
//        Log.d("ConnectActivity", "收到测量数据");
//        onReceiveScaleData(data);
//        QNScaleItemData fatValue = data.getItem(QNIndicator.TYPE_SUBFAT);
//        if (fatValue != null) {
//          String value = fatValue.getValue() + "";
//          Log.d("ConnectActivity", "收到皮下脂肪数据:" + value);
//        }
//        currentQNScaleData = data;
//        historyQNScaleData.add(data);
//        Log.d("ConnectActivity", "加密hmac为:" + data.getHmac());
////                Log.d("ConnectActivity", "收到体脂肪:"+data.getItem(QNIndicator.TYPE_BODYFAT).getValue());
//        doDisconnect();
//      }
//
//      @Override
//      public void onGetStoredScale(QNBleDevice device, List<QNScaleStoreData> storedDataList) {
//        Log.d("ConnectActivity", "收到存储数据");
//        if (storedDataList != null && storedDataList.size() > 0) {
//          QNScaleStoreData data = storedDataList.get(0);
//          for (int i = 0; i < storedDataList.size(); i++) {
//            Log.d("ConnectActivity", "收到存储数据:" + storedDataList.get(i).getWeight());
//          }
//          QNUser qnUser = createQNUser();
//          data.setUser(qnUser);
//          QNScaleData qnScaleData = data.generateScaleData();
//          onReceiveScaleData(qnScaleData);
//          currentQNScaleData = qnScaleData;
//          historyQNScaleData.add(qnScaleData);
//        }
//      }
//
//      @Override
//      public void onGetElectric(QNBleDevice device, int electric) {
//        String text = "收到电池电量百分比:" + electric;
//        Log.d("ConnectActivity", text);
//        if (electric == DecoderConst.NONE_BATTERY_VALUE) {//获取电池信息失败
//          return;
//        }
//        Toast.makeText(ConnectActivity.this, text, Toast.LENGTH_SHORT).show();
//      }
//
//      //测量过程中的连接状态
//      @Override
//      public void onScaleStateChange(QNBleDevice device, int status) {
//        Log.d("ConnectActivity", "秤的连接状态是:" + status);
//        setBleStatus(status);
//      }
//
//      @Override
//      public void onScaleEventChange(QNBleDevice device, int scaleEvent) {
//        Log.d("ConnectActivity", "秤返回的事件是:" + scaleEvent);
//      }
//    });
//  }

  public void setDataListener() {

    mQNBleApi.setDataListener(new QNScaleDataListener() {
      public double convertWeight(Double weight) {
        try {
          double finalWeight = weight * 1000;
          int unit = mQNBleApi.getConfig().getUnit();

            String convertedWeightAsString = mQNBleApi.convertWeightWithTargetUnit(weight,unit);
            double pounds = Float.valueOf(convertedWeightAsString.split("lb")[0]);
            double convertedWeight = 453.59237 * pounds;
            finalWeight = convertedWeight;

          return finalWeight;
        } catch(IllegalViewOperationException e) {
          return weight;
        }

      };

      @Override
      public void onGetUnsteadyWeight(QNBleDevice device, double weight) {
        Log.d("Drakos", "onGetUnsteadyWeight ");
        QNConfig mQnConfig = mQNBleApi.getConfig();
        double finalWeight = convertWeight(weight);

        WritableMap params = Arguments.createMap();
        params.putDouble("weight", finalWeight);
        params.putString("status", "sync");
        sendEventToJS("uploadProgress", params);
      }

//      @Override
//      public void onGetScaleData(QNBleDevice qnBleDevice, QNScaleData qnScaleData) {
//
//      }

//      @Override
//      public void onGetStoredScale(QNBleDevice qnBleDevice, List<QNScaleStoreData> list) {
//
//      }

      @Override
      public void onGetScaleData(QNBleDevice device, QNScaleData data) {
        WritableMap params = Arguments.createMap();
        params.putString("status", "complete");

        QNScaleItemData bmrValue = data.getItem(QNIndicator.TYPE_BMR);
        if (bmrValue != null) {
          Double value = bmrValue.getValue();
          params.putDouble("basalMetabolicRate", value);
        }

        QNScaleItemData visceralFatValue = data.getItem(QNIndicator.TYPE_VISFAT);
        if (visceralFatValue != null) {

          Double value = visceralFatValue.getValue();
          params.putDouble("visceralFatTanita", value);
        }

        QNScaleItemData weightValue = data.getItem(QNIndicator.TYPE_WEIGHT);
        if (weightValue != null) {
          double finalWeight = convertWeight(weightValue.getValue());

          params.putDouble("weight", finalWeight);
        }


        QNScaleItemData leanMassValue = data.getItem(QNIndicator.TYPE_LBM);
        if (leanMassValue != null) {
          Double value = leanMassValue.getValue() * 1000;
          params.putDouble("fatFreeMass", value);
        }

        QNScaleItemData bodyFatValue = data.getItem(QNIndicator.TYPE_BODYFAT);
        if (bodyFatValue != null) {
          Double value = bodyFatValue.getValue() * 1000;
          params.putDouble("bodyFat", value);
        }

        QNScaleItemData hydrationValue = data.getItem(QNIndicator.TYPE_WATER);
        if (hydrationValue != null) {
          Double value = hydrationValue.getValue() * 1000;
          params.putDouble("waterPercentage", value);
        }

        sendEventToJS("uploadProgress", params);
      }

      @Override
      public void onGetStoredScale(QNBleDevice device, List<QNScaleStoreData> storedDataList) {
      }

      @Override
      public void onGetElectric(QNBleDevice device, int electric) {
      }

      @Override
      public void onScaleStateChange(QNBleDevice device, int status) {
      }

      @Override
      public void onScaleEventChange(QNBleDevice qnBleDevice, int i) {
      }
    });
  }


  public void setDiscoveryListener() {
    Log.d("Drakos", "setDiscoveryListener 1");
    mQNBleApi.setBleDeviceDiscoveryListener(new QNBleDeviceDiscoveryListener() {
      @Override
      public void onDeviceDiscover(QNBleDevice device) {
        Log.d("Drakos", "setDiscoveryListener 2");
        mQNBleApi.connectDevice(device, createQNUser(), new QNResultCallback() {
          @Override
          public void onResult(int code, String msg) {
            Log.d("Drakos", "setDiscoveryListener 3");
            Log.d("onResult", "afdasf:" + msg);
          }
        });

      }

      @Override
      public void onStartScan() {
        Log.d("Drakos", "onStartScan");
//        QNLogUtils.log("ScanActivity", "onStartScan");
//        isScanning = true;
      }

      @Override
      public void onStopScan() {
        Log.d("Drakos", "onStopScan");
        mQNBleApi.stopBleDeviceDiscovery(new QNResultCallback() {
          @Override
          public void onResult(int code, String msg) {
            Log.d("Drakos", "onStopScan2 " + code);
            if (code == CheckStatus.OK.getCode()) {
            }
          }
        });
      }

      @Override
      public void onScanFail(int code) {
        Log.w("Scale", "onScanFail");
      }

      @Override
      public void onBroadcastDeviceDiscover(QNBleBroadcastDevice qnBleBroadcastDevice) {
        Log.w("Scale", "onBroadcastDeviceDiscover");

      }

      @Override
      public void onKitchenDeviceDiscover(QNBleKitchenDevice qnBleKitchenDevice) {
        Log.w("Scale", "onKitchenDeviceDiscover");
      }
    });
  }

  @ReactMethod
  public void onStartDiscovery(String name, final Promise promise) {
    Activity activity = getCurrentActivity();
    verifyPermissions(activity);
    Log.d("Drakos", "onStartDiscovery 1 ");
    Handler mHandler = new Handler();
    mHandler.post(new Runnable() {

      @Override
      public void run() {
        mQNBleApi.startBleDeviceDiscovery(new QNResultCallback() {
          @Override
          public void onResult(int code, String msg) {
            Log.d("Drakos", "onStartDiscovery 2 " + code);
            if (code != CheckStatus.OK.getCode()) {
              promise.resolve("Success scan scan: ");
            }

          }
        });
      }
    });
  }

  @ReactMethod
  public void stopScan(final Callback callback) {
    Log.d("Drakos", "stopScanstopScan ");
    mQNBleApi.stopBleDeviceDiscovery(new QNResultCallback() {
      @Override
      public void onResult(int code, String msg) {
        if (code == CheckStatus.OK.getCode()) {
          callback.invoke("stopScan: ");
        }
      }
    });

  }

  @ReactMethod
  public void onStopDiscovery() {
    Log.d("Drakos", "onStopDiscovery ");
    mQNBleApi.stopBleDeviceDiscovery(new QNResultCallback() {
      @Override
      public void onResult(int code, String msg) {

      }
    });

  }
}
