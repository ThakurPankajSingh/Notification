package com.reactnativeintegration;

import android.os.Bundle;
import android.util.Log;
import com.clevertap.android.sdk.CleverTapAPI;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.HashMap;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFcmMessage";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(TAG, token);

        // Register the new token with CleverTap
        CleverTapAPI cleverTapAPI = CleverTapAPI.getDefaultInstance(this);
        if (cleverTapAPI != null) {
            cleverTapAPI.pushFcmRegistrationId(token, true);
        }

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                return;
            }

            // Get new FCM registration token
            String newToken = task.getResult();
            Log.d(TAG, "MyFcmMessageListenerService: " + newToken);
        });
    }

    @Override
    public void onMessageReceived(RemoteMessage message) {
        super.onMessageReceived(message);
        Log.d(TAG, "Received FCM message: " + message.getData());

        try {
            if (!message.getData().isEmpty()) {
                // Convert the message data to a HashMap
                HashMap<String, String> dataMap = new HashMap<>(message.getData());
                Log.d(TAG, "Received FCM message: " + message.getData());

                Bundle extras = new Bundle();
                for (String key : dataMap.keySet()) {
                    extras.putString(key, dataMap.get(key));
                }

                // Pass the notification to CleverTap if applicable
                CleverTapAPI cleverTapAPI = CleverTapAPI.getDefaultInstance(this);
                if (cleverTapAPI != null) {
                    boolean fromCleverTap = cleverTapAPI.getNotificationInfo(extras).fromCleverTap;
                    if (fromCleverTap) {
                        cleverTapAPI.createNotification(getApplicationContext(), extras);
                    } else {
                        // Handle non-CleverTap notifications
                    }
                }
            }
        } catch (Throwable t) {
            Log.e(TAG, "Error handling FCM message", t);
        }
    }
}

