package com.enayet.bendroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.enayet.bendroid.R;

/**
 * Example shell activity which simply broadcasts to our receiver and exits.
 */
public class mStub extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = new Intent();
        i.setAction("com.enayet.bendroid.WEAR_NOTIFICATION");
        i.putExtra(wearNotificationReceiver.CONTENT_KEY, getString(R.string.title));
        sendBroadcast(i);
        finish();
    }
}
