package com.enayet.bendroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("BenDroid/AlarmReceiver", "Alarm received successfully"); //TODO: remove
        //Toast.makeText(AlarmReceiver.this, R.string.vibration_toast, Toast.LENGTH_SHORT).show(); //TODO: get context of toast
        Bundle extras = intent.getExtras();
        int mDuration = extras.getInt("vibrationDuration"); //Gets duration int from broadcaster
        Vibrator mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        mVibrator.vibrate(mDuration); //vibrate for milliseconds specified in argument
    }
}
