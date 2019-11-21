package com.salescube.healthcare.demo.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.salescube.healthcare.demo.NewMainActivity;
import com.salescube.healthcare.demo.NotificationActivity;
import com.salescube.healthcare.demo.R;
import com.salescube.healthcare.demo.data.model.NotificationModel;
import com.salescube.healthcare.demo.data.repo.NotificationRepo;
import com.salescube.healthcare.demo.func.DateFunc;

import java.util.Date;

import static android.media.RingtoneManager.getDefaultUri;

public class MessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyAndroidFCMService";
    private NotificationManager mNotificationManager;

    private NotificationCompat.Builder mBuilder;
    public static final String NOTIFICATION_CHANNEL_ID = String.valueOf(R.string.default_notification_channel_id);

    public MessagingService() {
        super();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

//        Log.d(TAG, "From: " + remoteMessage.getFrom());
//        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        //create notification

        if (remoteMessage.getNotification() != null) {
            String message = remoteMessage.getNotification().getBody();

            // Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        String title = remoteMessage.getData().get("title");
        String text =  remoteMessage.getData().get("msg");
        String type = remoteMessage.getData().get("type");
        String data = remoteMessage.getData().get("data");



//        Map<String, String> dataList = remoteMessage.getData();
//        for (Map.Entry<String, String> obj : dataList.entrySet()) {
//            Logger.i(obj.getKey() + "  " + obj.getValue());
//        }

        // createNotification(title, text);




        notify(title, text);
    }

    private void createNotification(String title, String messageBody) {

     /*   int requestID = (int) System.currentTimeMillis();

        Intent intent = new Intent(getApplicationContext(), NewMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent resultIntent = PendingIntent.getActivity(this, requestID, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri notificationSoundURI = getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_logo_3)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(notificationSoundURI)
                .setContentIntent(resultIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(54544, mNotificationBuilder.build());*/

    }

    //private final String CHANNEL_ID = "com.pitambaridigitalcare.b2c.notifier";

    private void notify(String title, String notificationMessage) {

        /**Creates an explicit intent for an Activity in your app**/
        Intent resultIntent = new Intent(getApplicationContext() , NotificationActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplicationContext(),
                0 /* Request code */, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder = new NotificationCompat.Builder(getApplicationContext());
        mBuilder.setSmallIcon(R.mipmap.ic_logo_3);
        mBuilder.setContentTitle(title)
                .setContentTitle(notificationMessage)
                .setAutoCancel(false)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(resultPendingIntent);

        mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(0 /* Request Code */, mBuilder.build());

        NotificationModel model = new NotificationModel();
        model.setTitle(title);
        model.setDate(DateFunc.getDateStr(new Date(), "yyyy-MM-dd"));
        model.setDescription(notificationMessage);

        NotificationRepo repo = new NotificationRepo();
        repo.insert(model);
    }

}

