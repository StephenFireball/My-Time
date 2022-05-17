package com.samsung.mytime;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class EventReminder extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "myTimeNotifications")
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setContentTitle("My Time")
                .setContentText("Name: " + EventEditActivity.eventName + "\nTime: " + EventEditActivity.time.toString())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(200, builder.build());
    }
}
