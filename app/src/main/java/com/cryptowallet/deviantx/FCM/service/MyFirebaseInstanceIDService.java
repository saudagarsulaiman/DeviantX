//package com.cryptowallet.deviantx.FCM.service;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Build;
//import android.support.annotation.NonNull;
//import android.support.v4.content.LocalBroadcastManager;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.cryptowallet.deviantx.FCM.app.Config;
//import com.cryptowallet.deviantx.R;
//import com.cryptowallet.deviantx.Utilities.CONSTANTS;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.FirebaseInstanceIdService;
//import com.google.firebase.iid.InstanceIdResult;
//
//public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
//
//    SharedPreferences pref;
//    static SharedPreferences.Editor editor;
//
//    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
////    public static String FCMToken = FirebaseInstanceId.getInstance().getToken();
//
//    @Override
//    public void onTokenRefresh() {
//        super.onTokenRefresh();
//
//        pref = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
//        editor = pref.edit();
//
////        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
////        String refreshedToken = getToken(getApplicationContext());
//
////        getToken(getApplicationContext());
//
//       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener((Activity) getApplicationContext(), new OnSuccessListener<InstanceIdResult>() {
//                @Override
//                public void onSuccess(InstanceIdResult instanceIdResult) {
//                    Log.e("Device Token", instanceIdResult.getToken());
//                    storeRegIdInPref(instanceIdResult.getToken());
//                }
//            });
//        } else {
//            String dToken = FirebaseInstanceId.getInstance().getToken();
//            storeRegIdInPref(dToken);
//        }*/
//        // [START retrieve_current_token]
//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "getInstanceId failed", task.getException());
//                            return;
//                        }
//
//                        // Get new Instance ID token
//                        String token = task.getResult().getToken();
//                        storeRegIdInPref(token);
//                    }
//                });
//        // [END retrieve_current_token]
//
///*
//        // Saving reg id to shared preferences
//        storeRegIdInPref(refreshedToken);
//*/
//
//        String refreshedToken = pref.getString(CONSTANTS.reg_ID, null);
//        // sending reg id to your server
//        sendRegistrationToServer(refreshedToken);
//
//        // Notify UI that registration has completed, so the progress indicator can be hidden.
//        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
//        registrationComplete.putExtra("token", refreshedToken);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
//    }
//
//    public static void getToken(Context applicationContext) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener((Activity) applicationContext, new OnSuccessListener<InstanceIdResult>() {
//                @Override
//                public void onSuccess(InstanceIdResult instanceIdResult) {
//                    Log.e("Device Token", instanceIdResult.getToken());
//                    storeRegIdInPref(instanceIdResult.getToken());
//                }
//            });
//        } else {
//            String dToken = FirebaseInstanceId.getInstance().getToken();
//            storeRegIdInPref(dToken);
//        }
//    }
//
//
//    private void sendRegistrationToServer(final String token) {
//        // sending gcm token to server
//        Log.e(TAG, "sendRegistrationToServer: " + token);
//    }
//
//    private static void storeRegIdInPref(String token) {
//        editor.putString(CONSTANTS.reg_ID, token);
//        editor.apply();
//    }
//}
