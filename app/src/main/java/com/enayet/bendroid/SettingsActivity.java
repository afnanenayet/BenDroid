package com.enayet.bendroid;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class SettingsActivity extends AppCompatActivity {

    private PendingIntent mPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

        //Intent to set up Alarm service
        Intent mAlarmIntent = new Intent(SettingsActivity.this, AlarmReceiver.class);
        mPendingIntent = PendingIntent.getBroadcast(SettingsActivity.this, 0, mAlarmIntent, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
           new AlertDialog.Builder(this)
                   .setTitle(R.string.dialog_about_title)
                   .setMessage(R.string.dialog_about_message)
                   .setCancelable(true)
                   .setPositiveButton(R.string.ok_label, null)
                   .show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setAlarm() {

    }
}
