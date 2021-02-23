package com.dam.petme.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.dam.petme.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationPublisher extends BroadcastReceiver {

    private static NotificationPublisher instance = null;
    private static final String PET_CHANNEL_ID = "pet_notification_channel";
    private static final int PET_NOTIFICATION_ID = 125;
    private NotificationManager mNotifyManager;

    public  NotificationPublisher() {

    }

    public static NotificationPublisher getInstance() {
        if(instance == null){
            instance = new NotificationPublisher();
        }
        return instance;
    }

    public void createNotificationChannel(Context context){
        mNotifyManager = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // Create a NotificationChannel
            NotificationChannel notificationChannel = new NotificationChannel(PET_CHANNEL_ID,
                    "Pet Notification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification Pet Upload");
            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        createNotificationChannel(context);
        showNotification(context);
    }

    private void showNotification(Context context) {

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_cat);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, PET_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_cat)
                .setLargeIcon(icon)
                .setContentTitle("Tu mascota ha sido cargada")
                .setContentText("Cuando haya noticias de su mascota te avisaremos!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(PET_NOTIFICATION_ID, builder.build());
    }
}