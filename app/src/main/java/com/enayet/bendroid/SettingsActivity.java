package com.enayet.bendroid;

import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {

    private PendingIntent mPendingIntent;
    SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences mPrefs, String key) {
            SettingsActivity mActivity = new SettingsActivity();
            mActivity.setAlarm();

            switch (key) {

                case ("military_time_pref"):
                    Log.i("Preferences", "Military time");
                    break;

                case ("exact_time_pref"):
                    break;

                case ("vibration_pref"):
                    break;

                case ("vibration_frequency_pref"):
                    break;
            }
        }
    };

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        FragmentManager mFragmentManager = getFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager
                .beginTransaction();
        SettingsFragment mPrefsFragment = new SettingsFragment();
        mFragmentTransaction.replace(android.R.id.content, mPrefsFragment);
        mFragmentTransaction.commit();
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        setAlarm();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mPrefs.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mPrefs.unregisterOnSharedPreferenceChangeListener(listener);
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
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mPrefs.registerOnSharedPreferenceChangeListener(listener);

        Log.i("BenDroid/Settings", "SetAlarm called successfully");
        Intent mAlarmIntent = new Intent(SettingsActivity.this, AlarmReceiver.class);

        mAlarmIntent.putExtra("singleVibration", mPrefs.getBoolean("vibration_frequency_pref", false));
        mAlarmIntent.putExtra("vibrationDuration", mPrefs.getInt("vibration_pref", 200)); //passing vibration duration to alarm receiver

        mPendingIntent = PendingIntent.getBroadcast(SettingsActivity.this, 0, mAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Creating alarm manager to make alarm service
        //This saves battery and and creates a separate thread to manage the alarm
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
