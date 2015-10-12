package com.enayet.bendroid;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("BenDroid/AlarmReceiver", "Alarm received successfully"); //TODO: remove

        Bundle extras = intent.getExtras();

        if (extras.getBoolean("sendNotification", true)) {
            sendNotification(context, extras.getBoolean("notificationSound"));
        }

        int mDuration = extras.getInt("vibrationDuration"); //Gets duration int from broadcaster
        Vibrator mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        mVibrator.vibrate(mDuration); //vibrate for milliseconds specified in argument
        mVibrator.cancel();
    }

    private void sendNotification(Context context, boolean notificationSound) {
        PendingIntent notificationIntent = PendingIntent.getActivity(context, 0,
                new Intent (context, SettingsActivity.class), 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.notification_icon) //TODO fix padding on notification icon
                .setContentTitle("Check the time!")
                .setContentText("It's been INSERT INTERVAL HERE")
                .setShowWhen(false);

        mBuilder.setContentIntent(notificationIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }
}
