package com.enayet.bendroid;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by aenayet on 11/26/15.
 */
public class stopButtonListener extends BroadcastReceiver {
    public void onReceive(Context context, Intent receiverIntent) {
        Log.d("Notification Listener", "Intent received");
        String actionName = receiverIntent.getAction();

        if (actionName.equals("stop_notifications")) {
            // Stopping alarm service
            Intent stopIntent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, stopIntent, 0);
            AlarmManager alarmManager = (AlarmManager)
                    context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            Log.d("Notification Listener", "Stopped alarm from notification");

            // Removing notification
            NotificationManager mManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            mManager.cancelAll();
            Log.d("Notification Listener", "Removed app notifications");

            // Setting preference
            SharedPreferences.Editor mEditor = PreferenceManager
                    .getDefaultSharedPreferences(context).edit();
            mEditor.putBoolean("is_service_enabled", false);
            mEditor.apply();
        }
    }
}
