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
    String encryptPath = "file:///android_asset/Lexington202004.qn";
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

    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission_group.NEARBY_DEVICES) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission_group.NEARBY_DEVICES}, 1);
      return;
    } else {
    }
  }

  private void setBleStatus(int bleStatus) {
    String stateString;
    String btnString;


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
        WritableMap params = Arguments.createMap();
        params.putString("status", "error");
        sendEventToJS("uploadProgress", params);
        setBleStatus(QNScaleStatus.STATE_DISCONNECTED);
      }
    });
  }

  public void sendEventToJS(String eventName, @Nullable WritableMap params) {
    this.reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
  }

  public void setDataListener() {

    mQNBleApi.setDataListener(new QNScaleDataListener() {
      public double convertWeight(Double weight) {
        try {
          double finalWeight = weight * 1000;
          int unit = mQNBleApi.getConfig().getUnit();

          if (unit == 0) {
            return finalWeight;
          } else {
            String convertedWeightAsString = mQNBleApi.convertWeightWithTargetUnit(weight,unit);
            double pounds = Float.valueOf(convertedWeightAsString.split("lb")[0]);
            double convertedWeight = 453.59237 * pounds;
            finalWeight = convertedWeight;
            return finalWeight;
          }
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
        params.putString("scaleId", device.getModeId());

        QNScaleItemData value = data.getItem(QNIndicator.TYPE_BMR);
        if (value != null) {
          params.putDouble("basalMetabolicRate", value.getValue());
        }

        value = data.getItem(QNIndicator.TYPE_VISFAT);
        if (value != null) {
          params.putDouble("visceralFatTanita", value.getValue());
        }

        value = data.getItem(QNIndicator.TYPE_WEIGHT);
        if (value != null) {
          double finalWeight = convertWeight(value.getValue());

          params.putDouble("weight", finalWeight);
        }

        value = data.getItem(QNIndicator.TYPE_LBM);
        if (value != null) {
          params.putDouble("fatFreeMass", value.getValue() * 1000);
        }

        value = data.getItem(QNIndicator.TYPE_BODYFAT);
        if (value != null) {
          params.putDouble("bodyFat", value.getValue() * 1000);
        }

        value = data.getItem(QNIndicator.TYPE_WATER);
        if (value != null) {
          params.putDouble("waterPercentage", value.getValue() * 1000);
        }

        // muscle mass	Muscle mass	Kg
        value = data.getItem(QNIndicator.TYPE_MUSCLE_MASS);
        if (value != null) {
          double finalWeight = convertWeight(value.getValue());
          params.putDouble("muscleMass", finalWeight);
        }

        // 	Skeletal muscle rate	%
        value = data.getItem(QNIndicator.TYPE_MUSCLE);
        if (value != null) {
          params.putDouble("skeletalMuscleRatio", value.getValue() * 1000);
        }

        value = data.getItem(QNIndicator.TYPE_BONE);
        if (value != null) {
          double finalWeight = convertWeight(value.getValue());
          params.putDouble("boneWeight", finalWeight);
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
          }
        });

      }

      @Override
      public void onStartScan() {
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
        WritableMap params = Arguments.createMap();
        params.putString("status", "error");
        sendEventToJS("uploadProgress", params);
        setBleStatus(QNScaleStatus.STATE_DISCONNECTED);
      }

      @Override
      public void onBroadcastDeviceDiscover(QNBleBroadcastDevice qnBleBroadcastDevice) {

      }

      @Override
      public void onKitchenDeviceDiscover(QNBleKitchenDevice qnBleKitchenDevice) {
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
            if (code == CheckStatus.OK.getCode()) {
              promise.resolve(true);
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
