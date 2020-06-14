package com.example.gainyourmuscle;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        //GET ID AND MESSAGE FROM INTENT
        int notificationId = intent.getIntExtra("notificationId",0);
        String message = intent.getStringExtra("todo");

        //When notification is tapped, call ScheduleFrag
        Intent schdIntent = new Intent(context,ScheduleFragment.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,0 , schdIntent, 0);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //Prepare notification
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Time to workout")
                .setContentText(message)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL);

         //Notify
        notificationManager.notify(notificationId, builder.build());
    }
}
