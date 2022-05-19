package com.samsung.mytime;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Notifications extends BroadcastReceiver {
    public static final int notificationID = 200;
    public static final String channelID = "myTimeNotifications";
    @Override
    public void onReceive(Context context, Intent intent) {
        android.app.Notification notification = new NotificationCompat.Builder(context, channelID)
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setContentTitle("My Time")
                .setContentText("Name: " + EventEditActivity.eventName + "\nTime: " + EventEditActivity.time.toString())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build();


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID, notification);
    }
}
