package com.enayet.bendroid;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {

    private PendingIntent mPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

        setAlarm();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) { //gives an informational dialog
           new AlertDialog.Builder(this)
                   .setTitle(R.string.dialog_about_title)
                   .setMessage(R.string.dialog_about_message)
                   .setCancelable(true)
                   .setPositiveButton(R.string.ok_label, null)
                   .show();

            return true;
        } else if (id == R.id.stop_alarm) { //allows the users to stop the alarm service
            killAlarm();
            Toast.makeText(SettingsActivity.this, "Service stopped", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void setAlarm() {
        Log.i("BenDroid/Settings", "SetAlarm called successfully");
        Intent mAlarmIntent = new Intent(SettingsActivity.this, AlarmReceiver.class);
        mAlarmIntent.putExtra("vibrationDuration", 500); //passing vibration duration to alarm receiver
        mPendingIntent = PendingIntent.getBroadcast(SettingsActivity.this, 0, mAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Creating alarm manager to make alarm service
        //This saves battery and allows the com.enayet.bendroid thread to die but keep the service going
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        //setting starting time and periodic intervals of alarm
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        mCalendar.add(Calendar.SECOND, 0); // first time
        long frequency = 60 * 1000; // in ms

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), frequency, mPendingIntent);
    }

    public void killAlarm() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        Log.i("BenDroid/Settings", "Alarm stopped succesfully");
    }
}
