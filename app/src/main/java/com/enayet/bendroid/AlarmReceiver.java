package com.enayet.bendroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //intent.getIntExtra("vibrationDuration", 1); //TODO get int from intent, use that as vibration length
        Vibrator mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        mVibrator.vibrate(500); //vibrate for milliseconds specified in argument TODO change duration to variable
    }
}
