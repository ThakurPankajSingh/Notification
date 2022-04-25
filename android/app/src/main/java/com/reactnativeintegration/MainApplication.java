package com.reactnativeintegration;

import com.facebook.react.modules.core.DeviceEventManagerModule;
import java.util.Iterator;
import java.util.*;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import com.facebook.react.ReactApplication;
import com.clevertap.android.sdk.pushnotification.CTPushNotificationListener;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.PackageList;
import com.clevertap.android.sdk.ActivityLifecycleCallback;
import com.clevertap.android.sdk.CleverTapAPI;
import com.facebook.soloader.SoLoader;
import com.reactnativeintegration.MainApplication;
import android.os.Looper;
import android.util.Log;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.ReactInstanceManager.ReactInstanceEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;
import com.facebook.react.bridge.WritableMap;
import org.json.JSONObject;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class MainApplication extends Application implements ReactApplication, CTPushNotificationListener {

  private final ReactNativeHost mReactNativeHost =
      new ReactNativeHost(this) {
        @Override
        public boolean getUseDeveloperSupport() {
          return BuildConfig.DEBUG;
        }

        @Override
        protected List<ReactPackage> getPackages() {
          @SuppressWarnings("UnnecessaryLocalVariable")
          List<ReactPackage> packages = new PackageList(this).getPackages();
          // Packages that cannot be autolinked yet can be added manually here, for example:
          // packages.add(new MyReactNativePackage());
          // packages.add(new CleverTapPackage());
          return packages;
        }

        @Override
        protected String getJSMainModuleName() {
          return "index";
        }
      };

  @Override
  public ReactNativeHost getReactNativeHost() {
    return mReactNativeHost;
  }

  private static final String TAG = "CleverTapApplication";
  @Override
  public void onCreate() {

    // CleverTapAPI.setUIEditorConnectionEnabled(false);
    ActivityLifecycleCallback.register(this); 
    CleverTapAPI.getDefaultInstance(getApplicationContext()).enableDeviceNetworkInfoReporting(true);
    CleverTapAPI.setDebugLevel(3);
    super.onCreate();
    SoLoader.init(this, /* native exopackage */ false);
    CleverTapAPI.getDefaultInstance(this).setCTPushNotificationListener(this);
    initializeFlipper(this, getReactNativeHost().getReactInstanceManager());
  }

  @Override
    public void onNotificationClickedPayloadReceived(final HashMap<String, Object> payload) {
        Log.e(TAG, "onNotificationClickedPayloadReceived called");
        final String CLEVERTAP_PUSH_NOTIFICATION_CLICKED = "CleverTapPushNotificationClicked";

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {

                // Construct and load our normal React JS code bundle
                final ReactInstanceManager mReactInstanceManager = ((ReactApplication) getApplicationContext())
                        .getReactNativeHost().getReactInstanceManager();
                ReactContext context = mReactInstanceManager.getCurrentReactContext();
                // If it's constructed, send a notification
                if (context != null) {
                    sendEvent(CLEVERTAP_PUSH_NOTIFICATION_CLICKED, getWritableMapFromMap(payload), context);
                } else {
                    // Otherwise wait for construction, then send the notification
                    mReactInstanceManager
                            .addReactInstanceEventListener(new ReactInstanceManager.ReactInstanceEventListener() {
                                public void onReactContextInitialized(ReactContext context) {
                                    sendEvent(CLEVERTAP_PUSH_NOTIFICATION_CLICKED, getWritableMapFromMap(payload),
                                            context);
                                    mReactInstanceManager.removeReactInstanceEventListener(this);
                                }
                            });
                    if (!mReactInstanceManager.hasStartedCreatingInitialContext()) {
                        // Construct it in the background
                        mReactInstanceManager.createReactContextInBackground();
                    }
                }

            }
        });

    }

    private void sendEvent(String eventName, Object params, ReactContext context) {
        try {
            context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(eventName, params);
            Log.e(TAG, "Sending event "+eventName);
        } catch (Throwable t) {
            Log.e(TAG, t.getLocalizedMessage());
        }
    }

    public static WritableMap getWritableMapFromMap(Map<String, ? extends Object> var1) {
        JSONObject extras = var1 != null ? new JSONObject(var1) : new JSONObject();
        WritableMap extrasParams = Arguments.createMap();
        Iterator extrasKeys = extras.keys();
        while (extrasKeys.hasNext()) {
            String key = null;
            String value = null;
            try {
                key = extrasKeys.next().toString();
                value = extras.get(key).toString();
            } catch (Throwable t) {
                Log.e(TAG, t.getLocalizedMessage());
            }

            if (key != null && value != null) {
                extrasParams.putString(key, value);
            }
        }
        return extrasParams;
    }


  

  /**
   * Loads Flipper in React Native templates. Call this in the onCreate method with something like
   * initializeFlipper(this, getReactNativeHost().getReactInstanceManager());
   *
   * @param context
   * @param reactInstanceManager
   */
  private static void initializeFlipper(
      Context context, ReactInstanceManager reactInstanceManager) {
    if (BuildConfig.DEBUG) {
      try {
        /*
         We use reflection here to pick up the class that initializes Flipper,
        since Flipper library is not available in release mode
        */
        Class<?> aClass = Class.forName("com.reactnativeintegration.ReactNativeFlipper");
        aClass
            .getMethod("initializeFlipper", Context.class, ReactInstanceManager.class)
            .invoke(null, context, reactInstanceManager);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
    }
  }
}
