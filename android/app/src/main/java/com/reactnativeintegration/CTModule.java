package com.reactnativeintegration;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;
import com.clevertap.android.sdk.ActivityLifecycleCallback;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.CleverTapInstanceConfig;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.io.*;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;
import java.util.*;
import com.facebook.react.bridge.*;

public class CTModule extends ReactContextBaseJavaModule {
    Context context;

    CTModule(ReactApplicationContext context) {
        super(context);
        this.context = context;
    }

    @ReactMethod
    CleverTapAPI clevertapAdditionalInstance = null;
    CleverTapInstanceConfig clevertapAdditionalInstanceConfig = null;

    @NonNull
    @Override
    public String getName() {
        return "CTModule";
    }

    @ReactMethod
    void initCleverTap(String country) {
        Log.d("CT", "I am from CTModule initCleverTap Method");
        if (Objects.equals(country, "KSA")) {

            clevertapAdditionalInstanceConfig = CleverTapInstanceConfig.createInstance(
                    getReactApplicationContext(),
                    "W67-9W5-9R7Z",
                    "50c-a60"
            );

            clevertapAdditionalInstanceConfig.setDebugLevel(CleverTapAPI.LogLevel.VERBOSE);
            clevertapAdditionalInstanceConfig.useGoogleAdId(false);
            clevertapAdditionalInstanceConfig.enablePersonalization(false);

            clevertapAdditionalInstance = CleverTapAPI.instanceWithConfig(getReactApplicationContext(), clevertapAdditionalInstanceConfig);

        } else if (Objects.equals(country, "UAE")) {
            clevertapAdditionalInstanceConfig = CleverTapInstanceConfig.createInstance(
                    getReactApplicationContext(),
                    "W67-9W5-9R7Z",
                    "50c-a60"
            );

            clevertapAdditionalInstanceConfig.setDebugLevel(CleverTapAPI.LogLevel.VERBOSE);
            clevertapAdditionalInstanceConfig.useGoogleAdId(false);
            clevertapAdditionalInstanceConfig.enablePersonalization(false);

            clevertapAdditionalInstance = CleverTapAPI.instanceWithConfig(getReactApplicationContext(), clevertapAdditionalInstanceConfig);

        }
    }

    @ReactMethod
    public void raiseEvent(String eventName , ReadableMap props) {
        Map<String, Object> finalProps = eventPropsFromReadableMap(props, Object.class);
           if (finalProps == null) {
               clevertapAdditionalInstance.pushEvent(eventName);
           } else {
               clevertapAdditionalInstance.pushEvent(eventName, finalProps);
           }
    }

    @ReactMethod
    CleverTapAPI returnCTInstance() {
        System.out.println("clevertapAdditionalInstance: "+clevertapAdditionalInstance.toString());
        return clevertapAdditionalInstance;
    }

    @ReactMethod
    void callOnUserLogin() {
        HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
        profileUpdate.put("Name", "React User 4");    // String
        profileUpdate.put("Identity", "reactUser4");      // String or number
        profileUpdate.put("Email", "reactuser4@test.com"); // Email address of the user
        profileUpdate.put("Phone", "+14155551234");                 //String Array

        clevertapAdditionalInstance.onUserLogin(profileUpdate);
    }

    @ReactMethod
    public void resurrectApp(){
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        if (launchIntent != null) {
            context.startActivity(launchIntent);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private <T> HashMap<String, T> eventPropsFromReadableMap(ReadableMap propsMap, Class<T> tClass) {
        if (propsMap == null) {
            return null;
        }

        HashMap<String, T> props = new HashMap<>();

        ReadableMapKeySetIterator iterator = propsMap.keySetIterator();

        while (iterator.hasNextKey()) {
            try {
                String key = iterator.nextKey();
                ReadableType readableType = propsMap.getType(key);

                if (readableType == ReadableType.String) {
                    props.put(key, tClass.cast(propsMap.getString(key)));
                } else if (readableType == ReadableType.Boolean) {
                    props.put(key, tClass.cast(propsMap.getBoolean(key)));
                } else if (readableType == ReadableType.Number) {
                    try {
                        props.put(key, tClass.cast(propsMap.getDouble(key)));
                    } catch (Throwable t) {
                        try {
                            props.put(key, tClass.cast(propsMap.getInt(key)));
                        } catch (Throwable t1) {
                            Log.e("CleverTapReactNative", "Unhandled ReadableType.Number from ReadableMap");
                        }
                    }
                } else {
                    Log.e("CleverTapReactNative", "Unhandled event property ReadableType");
                }
            } catch (Throwable t) {
                Log.e("CleverTapReactNative", t.getLocalizedMessage());
            }
        }
        return props;
    }
}