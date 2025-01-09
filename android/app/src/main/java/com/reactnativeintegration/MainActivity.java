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


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MainActivity extends ReactActivity implements PushPermissionResponseListener {

    // CleverTap instance
    private CleverTapAPI clevertapDefaultInstance;

    @Override
    protected String getMainComponentName() {
        return "ReactNativeIntegration";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize CleverTap
        CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.DEBUG); // Optional: Set debug level
        clevertapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());

        if (clevertapDefaultInstance != null) {
            clevertapDefaultInstance.registerPushPermissionNotificationResponseListener(this);
        }

        // Handle push notification for background intent
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            CleverTapAPI.createNotification(getApplicationContext(), intent.getExtras());
           // clevertapDefaultInstance.setCTPushNotificationListener((extras, fromPush) -> {
                String title = intent.getExtras().get("nt").toString();
                String message = intent.getExtras().get("nm").toString();
                showNotification(title, message);
           // });
        }
    }

    /**
     * Custom method to show notifications (like the above example)
     */
    private void showNotification(String title, String message) {
        String channelId = "notification_channel";
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.baseline_notification_important_24)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "CleverTap Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, builder.build());
    }

    @Override
    public void onPushPermissionResponse(boolean accepted) {
        Log.i("RN", "onPushPermissionResponse :  InApp---> response() called accepted="+accepted);
    }
}





//public class MainActivity extends ReactActivity implements PushPermissionResponseListener {
//
//  /**
//   * Returns the name of the main component registered from JavaScript. This is used to schedule
//   * rendering of the component.
//   */
//
//
//  // CleverTapAPI clevertapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());
//
//
//  @Override
//  protected String getMainComponentName() {
//    return "ReactNativeIntegration";
//  }
//
//  @Override
//	protected void onCreate(Bundle savedInstanceState) {
//    	super.onCreate(savedInstanceState);
////    	CleverTapModule.setInitialUri(getIntent().getData());
//        CleverTapAPI.getDefaultInstance(this).registerPushPermissionNotificationResponseListener(this);
//
//
//      // CleverTapAPI.setNotificationHandler((NotificationHandler)new PushTemplateNotificationHandler());
//	}
//
//
//  @Override
//   public void onNewIntent(Intent intent) {
//       super.onNewIntent(intent);
//       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//          CleverTapAPI.getDefaultInstance(getApplicationContext()).pushNotificationClickedEvent(intent.getExtras());
//       }
//    }
//
//    @Override
//    public void onResume() {
//      super.onResume();
//      JSONObject jsonObject = CTLocalInApp.builder()
//        .setInAppType(CTLocalInApp.InAppType.ALERT)
//        .setTitleText("Get Notified")
//        .setMessageText("Enable Notification permission")
//        .followDeviceOrientation(true)
//        .setPositiveBtnText("Allow")
//        .setNegativeBtnText("Cancel")
//        .build();
//CleverTapAPI.getDefaultInstance(this).promptPushPrimer(jsonObject);
//    }
//
//    @Override
//    public void onPushPermissionResponse(boolean accepted) {
//        Log.i("RN", "onPushPermissionResponse :  InApp---> response() called accepted="+accepted);
//    }
//
//}