package com.cryptowallet.deviantx.FCM.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Activities.DashBoardActivity;
import com.google.android.gms.gcm.GcmListenerService;
import com.parse.PLog;

import org.json.JSONException;
import org.json.JSONObject;

public class ParseGCMListenerService extends GcmListenerService {

    @Override
    public void onMessageReceived(String s, Bundle bundle) {
        super.onMessageReceived(s, bundle);
        String pushId = bundle.getString("push_id");
        String timestamp = bundle.getString("time");
        String dataString = bundle.getString("data");
        String channel = bundle.getString("channel");
        Log.d("Notification_data--> ", pushId);

        JSONObject data = null;
        if (dataString != null) {
            try {
                data = new JSONObject(dataString);
                handleNotification(data, timestamp);
            } catch (JSONException e) {
                PLog.e(ParseGCM.TAG, "Ignoring push because of JSON exception while processing: " + dataString, e);
                return;
            }
        }

        // PushRouter.getInstance().handlePush(pushId, timestamp, channel, data);
    }

    private void handleNotification(JSONObject json, String timestamp) {

        try {

            JSONObject data = json;

//            String title = data.has("title") ? data.getString("title") : "";
            String title = getApplicationContext().getResources().getString(R.string.app_name);
            String message = data.has("alert") ? data.getString("alert").replace("%20", " ") : "";
            if (message.equals("")) {
                message = data.has("%20body") ? data.getString("%20body").replace("%20", " ") : "";
            }

            title = getApplicationContext().getResources().getString(R.string.app_name);


            sendNotification(title, message);


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Showing notification with text only
     */
/*
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, JSONObject data) {
        int NOTIFICATION_ID = 0;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent connectIntentMain = ((ShareGApplication) getApplication()).getPendingIntent();
        Boolean isNavigateToNetwork = false;
        String ssidName = "";
        try {
            if (data.has("isNavigateToNetwork"))
                isNavigateToNetwork = data.getBoolean("isNavigateToNetwork");
            if (data.has("SSID"))
                ssidName = data.getString("SSID");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Connect
        Intent connectIntent = new Intent(getBaseContext(), WifiCoinNotificationReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putString("ssidName", ssidName);
        bundle.putInt("notificationId", NOTIFICATION_ID);
        connectIntent.putExtras(bundle);
        if (UI.Singleton.mInstance.mViewPager != null && isNavigateToNetwork)
            connectIntentMain = PendingIntent.getBroadcast(getBaseContext(), 0, connectIntent, Intent.FILL_IN_DATA);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this);
        notification.setContentTitle(title);
        notification.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        notification.setContentText(message);
        notification.setOngoing(true);
        notification.setOnlyAlertOnce(true);
        notification.setSmallIcon(getNotificationIcon(notification));
        notification.setContentIntent(connectIntentMain);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId("WifiCoin_01");
            NotificationChannel channel = new NotificationChannel("WifiCoin_01",
                    "Wificoin Notification",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(NOTIFICATION_ID, notification.build());


    }
*/
    private void sendNotification(String messageBody, String title) {
        Intent intent = new Intent(this, DashBoardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.app_icon)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
//                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

/*
    private int getNotificationIcon(NotificationCompat.Builder notificationBuilder) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = 0x008000;
            notificationBuilder.setColor(color);
            return R.drawable.ic_notification_shellnet;

        }
        return R.drawable.ic_notification_shellnet;
    }
*/
   /* private void showNotificationMessage(Context context, String title, String message, String timeStamp) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Get the layouts to use in the custom notification
        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.notification_small);
        RemoteViews notificationLayoutExpanded = new RemoteViews(getPackageName(), R.layout.notification_large);
        notificationLayout.setTextViewText(R.id.msg, message);
        notificationLayoutExpanded.setTextViewText(R.id.msg, message);

// Apply the layouts to the notification
        Notification customNotification = new NotificationCompat.Builder(context, "WifiCoin_01")
                .setSmallIcon(R.drawable.ic_notification_shellnet)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationLayout)
                .setCustomBigContentView(notificationLayoutExpanded)
                .setContentIntent(((ShareGApplication) getApplication()).getPendingIntent())
                .build();

        notificationManager.notify(0, customNotification);


    }*/

}