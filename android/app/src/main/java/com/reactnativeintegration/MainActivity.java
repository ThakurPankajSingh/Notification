package com.reactnativeintegration;

import com.facebook.react.ReactActivity;
import com.clevertap.react.CleverTapModule;
import com.clevertap.android.sdk.CleverTapAPI;
import android.os.Bundle;
import android.content.Intent;
import android.os.Build;
import java.util.*;
import com.clevertap.android.sdk.PushPermissionResponseListener;
import com.clevertap.android.sdk.inapp.CTLocalInApp;
import org.json.*;
import android.util.Log;

public class MainActivity extends ReactActivity implements PushPermissionResponseListener {

  /**
   * Returns the name of the main component registered from JavaScript. This is used to schedule
   * rendering of the component.
   */


  // CleverTapAPI clevertapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());


  @Override
  protected String getMainComponentName() {
    return "ReactNativeIntegration";
  }

  @Override
	protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
//    	CleverTapModule.setInitialUri(getIntent().getData());
        CleverTapAPI.getDefaultInstance(this).registerPushPermissionNotificationResponseListener(this);
        

      // CleverTapAPI.setNotificationHandler((NotificationHandler)new PushTemplateNotificationHandler());
	}


  @Override
   public void onNewIntent(Intent intent) {
       super.onNewIntent(intent);
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//           CleverTapAPI.getDefaultInstance(getApplicationContext()).pushNotificationClickedEvent(intent.getExtras());
       }
    }

    @Override
    public void onResume() {
      super.onResume();
      JSONObject jsonObject = CTLocalInApp.builder()
        .setInAppType(CTLocalInApp.InAppType.ALERT)
        .setTitleText("Get Notified")
        .setMessageText("Enable Notification permission")
        .followDeviceOrientation(true)
        .setPositiveBtnText("Allow")
        .setNegativeBtnText("Cancel")
        .build();
CleverTapAPI.getDefaultInstance(this).promptPushPrimer(jsonObject);
    }

    @Override
    public void onPushPermissionResponse(boolean accepted) {
        Log.i("RN", "onPushPermissionResponse :  InApp---> response() called accepted="+accepted);
    }

}
