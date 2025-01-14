package com.reactnativeintegration;

import com.facebook.react.ReactActivity;
import com.clevertap.android.sdk.CleverTapAPI;
import android.os.Bundle;
import java.util.*;
import com.clevertap.android.sdk.PushPermissionResponseListener;
import com.google.firebase.messaging.FirebaseMessaging;
import android.util.Log;

public class MainActivity extends ReactActivity implements PushPermissionResponseListener {

  @Override
  protected String getMainComponentName() {
    return "ReactNativeIntegration";
  }

  @Override
	protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);

        CleverTapAPI.getDefaultInstance(this).registerPushPermissionNotificationResponseListener(this);


      CleverTapAPI cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());

      createUserProfile();
      registerPushNotifications();
	}

    private void createUserProfile() {
        HashMap<String, Object> profileUpdate = new HashMap<>();
        profileUpdate.put("Name", "Pankaj"); // String
        profileUpdate.put("Identity", 20213636); // String or number
        profileUpdate.put("Email", "pankaj8@gmail.com"); // Email address of the user
        profileUpdate.put("Phone", "+918899948888"); // Phone (with the country code, starting with +)

        CleverTapAPI clevertapInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());
        if (clevertapInstance != null) {
            clevertapInstance.onUserLogin(profileUpdate);
        }
    }

    private void registerPushNotifications() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("Nisha", "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();
                    System.out.println("token " + token);

                    CleverTapAPI clevertapInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());
                    if (clevertapInstance != null) {
                        clevertapInstance.pushFcmRegistrationId(token, true);
                    }
                });
    }

    @Override
    public void onPushPermissionResponse(boolean accepted) {
        Log.i("RN", "onPushPermissionResponse :  InApp---> response() called accepted="+accepted);
    }

}