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
//        clevertapAdditionalInstance = CleverTapAPI.getDefaultInstance(getReactApplicationContext());
        if (Objects.equals(country, "KSA")) {

            clevertapAdditionalInstanceConfig = CleverTapInstanceConfig.createInstance(
                    getReactApplicationContext(),
                    "TEST-W8W-6WR-846Z",
                    "TEST-206-0b0"
            );

            clevertapAdditionalInstanceConfig.setDebugLevel(CleverTapAPI.LogLevel.VERBOSE);
            clevertapAdditionalInstanceConfig.useGoogleAdId(false);
            clevertapAdditionalInstanceConfig.enablePersonalization(false);

            clevertapAdditionalInstance = CleverTapAPI.instanceWithConfig(getReactApplicationContext(), clevertapAdditionalInstanceConfig);


//            CleverTapAPI.changeCredentials("TEST-W8W-6WR-846Z", "TEST-206-0b0");
//            CleverTapAPI.setAppForeground(true);
//            ActivityLifecycleCallback.register(getCurrentActivity().getApplication());

        } else if (Objects.equals(country, "UAE")) {
            clevertapAdditionalInstanceConfig = CleverTapInstanceConfig.createInstance(
                    getReactApplicationContext(),
                    "TEST-RK4-66R-966Z",
                    "TEST-266-432"
            );

            clevertapAdditionalInstanceConfig.setDebugLevel(CleverTapAPI.LogLevel.VERBOSE);
            clevertapAdditionalInstanceConfig.useGoogleAdId(false);
            clevertapAdditionalInstanceConfig.enablePersonalization(false);

            clevertapAdditionalInstance = CleverTapAPI.instanceWithConfig(getReactApplicationContext(), clevertapAdditionalInstanceConfig);
//            ActivityLifecycleCallback.register(getCurrentActivity().getApplication());

//            CleverTapAPI.changeCredentials("TEST-W8W-6WR-846Z","TEST-206-0b0" );
//            CleverTapAPI.setAppForeground(true);
//            ActivityLifecycleCallback.register(getCurrentActivity().getApplication());

        }
    }

    @ReactMethod
    CleverTapAPI returnCTInstance() {
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


    // pass you reference from here

}
