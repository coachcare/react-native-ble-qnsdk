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
import com.yolanda.health.qnblesdk.constant.QNUnit;
import com.yolanda.health.qnblesdk.listener.QNBleConnectionChangeListener;
import com.yolanda.health.qnblesdk.listener.QNScaleDataListener;
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
import com.yolanda.health.qnblesdk.out.QNBleKitchenDevice;
import com.yolanda.health.qnblesdk.out.QNConfig;
import com.yolanda.health.qnblesdk.out.QNScaleData;
import com.yolanda.health.qnblesdk.out.QNScaleItemData;
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
  public User mUser = new User();
  private boolean loaded = false;
  public static final String FORMAT_SHORT = "yyyy-MM-dd";


  public BleQnsdkModule(ReactApplicationContext reactContext) {

    super(reactContext);

    reactContext.addLifecycleEventListener(this);
    this.reactContext = reactContext;
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

  public void setConnectionListener() {
    final ReactApplicationContext context = getReactApplicationContext();
    mQNBleApi.setBleConnectionChangeListener(new QNBleConnectionChangeListener() {
      @Override
      public void onConnecting(QNBleDevice device) {
      }

      @Override
      public void onConnected(QNBleDevice device) {
      }

      @Override
      public void onServiceSearchComplete(QNBleDevice device) {
      }

      //The connection is being disconnected. When the disconnection is called, it will be called back immediately.
      @Override
      public void onDisconnecting(QNBleDevice device) {
      }

      //Disconnect, callback after disconnect
      @Override
      public void onDisconnected(QNBleDevice device) {
      }

      //There was a connection error, please refer to the attached table for the error code
      @Override
      public void onConnectError(QNBleDevice device, int errorCode) {
      }
    });

  };

  public void sendEventToJS(String eventName, @Nullable WritableMap params) {
    this.reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
  }

  public void setDataListener() {

    mQNBleApi.setDataListener(new QNScaleDataListener() {
      public double convertWeight(Double weight) {
        try {
          double finalWeight = weight * 1000;
          if (mQNBleApi.getConfig().getUnit() == QNUnit.WEIGHT_UNIT_LB) {
            String convertedWeightAsString = mQNBleApi.convertWeightWithTargetUnit(weight,QNUnit.WEIGHT_UNIT_LB);
            double pounds = Float.valueOf(convertedWeightAsString.split("lb")[0]);
            double convertedWeight = 453.59237 * pounds;
            finalWeight = convertedWeight;
          }

          return finalWeight;
        } catch(IllegalViewOperationException e) {
          return weight;
        }

      };

      @Override
      public void onGetUnsteadyWeight(QNBleDevice device, double weight) {
        QNConfig mQnConfig = mQNBleApi.getConfig();
        double finalWeight = convertWeight(weight);

        WritableMap params = Arguments.createMap();
        params.putDouble("weight", finalWeight);
        params.putString("status", "sync");
        sendEventToJS("uploadProgress", params);
      }

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
    mQNBleApi.setBleDeviceDiscoveryListener(new QNBleDeviceDiscoveryListener() {
      @Override
      public void onDeviceDiscover(QNBleDevice device) {
        mQNBleApi.connectDevice(device, createQNUser(), new QNResultCallback() {
          @Override
          public void onResult(int code, String msg) {
            Log.d("onResult", "afdasf:" + msg);
          }
        });

      }

      @Override
      public void onStartScan() {
//        QNLogUtils.log("ScanActivity", "onStartScan");
//        isScanning = true;
      }

      @Override
      public void onStopScan() {
        mQNBleApi.stopBleDeviceDiscovery(new QNResultCallback() {
          @Override
          public void onResult(int code, String msg) {
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

    Handler mHandler = new Handler();
    mHandler.post(new Runnable() {

      @Override
      public void run() {
        mQNBleApi.startBleDeviceDiscovery(new QNResultCallback() {
          @Override
          public void onResult(int code, String msg) {
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
    mQNBleApi.stopBleDeviceDiscovery(new QNResultCallback() {
      @Override
      public void onResult(int code, String msg) {
        
      }
    });
    
  }
}
