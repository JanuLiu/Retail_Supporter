package com.example.retailsupporter.Controller;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import com.example.retailsupporter.R;


public class AlarmReceiver extends BroadcastReceiver {

    public static final String NOTIFY_ID = "notyfy_id";
    public static final int NOTYFI_REQUEST_ID = 300;
    public static final int NOTIFY_ID_NUM = 11;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("AlarmReceiver", "AlarmReceiver - Active");
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                NOTYFI_REQUEST_ID,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("Retail Supporter - Clock Out Notification")
                .setContentText("Don't forget Clock out!")
                .setContentIntent(pendingIntent);
        NotificationChannel channel;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(NOTIFY_ID
                    , "Notify"
                    , NotificationManager.IMPORTANCE_HIGH);
            builder.setChannelId(NOTIFY_ID);
            notifyManager.createNotificationChannel(channel);
        } else {
            builder.setDefaults(Notification.DEFAULT_ALL)
                    .setVisibility(Notification.VISIBILITY_PUBLIC);
        }
        notifyManager.notify(NOTIFY_ID_NUM, builder.build());
    }
}

