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
            sendNotification(context, extras.getBoolean("notificationSound", true), extras.getString("intervalUnit"));
        }

        if (extras.getBoolean("shouldVibrate", false)) {
            Vibrator mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            mVibrator.vibrate(extras.getInt("vibrationDuration")); //vibrate for milliseconds specified in argument
        }
    }

    private void sendNotification(Context context, boolean notificationSound, String intervalUnit) {
        final int LED_COLOR = 12211667; //sets the color of the notification led (PURPLE)
        Intent stopNotifications = new Intent();
        stopNotifications.setAction("stop_notifications");
        PendingIntent stopPending = PendingIntent.getBroadcast(context, 0, stopNotifications,
                PendingIntent.FLAG_UPDATE_CURRENT);


        PendingIntent notificationIntent = PendingIntent.getActivity(context, 0,
                new Intent (context, SettingsActivity.class), 0);

        // TODO add pending intent/receiver for broadcast of "stop" action to stopButtonListener

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notification)
                .setLights(LED_COLOR, 200, 100)
                .setColor(LED_COLOR) //sets notification background color
                .setContentTitle("Check the time!")
                .setContentText("It's been " + intervalUnit + " minutes")
                .setShowWhen(false)
                .addAction(R.drawable.ic_stop_24dp,
                        context.getResources().getString(R.string.quit_notifications), stopPending)
                .setAutoCancel(true); //deletes timestamp TODO dismiss alarm from notification

        mBuilder.setContentIntent(notificationIntent);

        // Adding notification properties from user preferences
        if (notificationSound) {
            mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        }

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }
}
