package com.salescube.healthcare.demo.service;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.salescube.healthcare.demo.func.SharePref;
import com.salescube.healthcare.demo.sysctrl.AppControl;

public class FcmFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";
    AppControl ctrl;

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, " " + refreshedToken);
        sendRegistrationToServer(refreshedToken);

        storeRegIdInPref(refreshedToken);

    }

    private void sendRegistrationToServer(String refreshedToken) {
        Log.d(TAG, "Refreshed token: " + refreshedToken);
    }


    private void storeRegIdInPref(String token) {

        SharePref pref = new SharePref(getApplicationContext());

       pref.setToken(token);
       pref.setTokenStatus("1");
    }

}

