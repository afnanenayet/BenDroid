package com.enayet.bendroid;

import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {

    private PendingIntent mPendingIntent;
    SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences mPrefs, String key) {
            if (mPrefs.getBoolean("is_service_enabled", true)) {
                setAlarm();
            } else {
                killAlarm();
            }
        }
    };

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Getting preference fragment and populating content of this activity with it
        FragmentManager mFragmentManager = getFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager
                .beginTransaction();
        SettingsFragment mPrefsFragment = new SettingsFragment();
        mFragmentTransaction.replace(android.R.id.content, mPrefsFragment, "FRAGMENT_SETTINGS");
        mFragmentTransaction.commit();
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
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
        // Registering preference listener so it won't get caught by garbage collection
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mPrefs.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregistering preference listener since we won't be using when the user isn't
        // focused on the activity
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
        }
        return super.onOptionsItemSelected(item);
    }

    // Creates the Alarm service which specifies what times
    public void setAlarm() {
        // Parsing preferences in another thread to avoid hogging resources of main/UI thread
        Handler mHandler = new Handler();
        Runnable mRun = new Runnable() {

            @Override
            public void run() {
                SharedPreferences mPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getApplicationContext());
                mPrefs.registerOnSharedPreferenceChangeListener(listener);

                // Preference IDs
                final String vibrationFrequency = getString(R.string.key_vibration_frequency_pref);
                final String vibrationDuration = getString(R.string.key_vibration_pref);
                final String notificationPreference = getString(R.string.key_send_notification);
                final String vibrationPreference = getString(R.string.key_vibrate_notification);
                final String intervalPreference = getString(R.string.key_interval_pref);
                final String exactTimePreference = getString(R.string.key_exact_time_pref);

                Log.d("BenDroid/Service", "Alarm service set");
                //creating intent for alarm which will trigger vibration/notification
                Intent mAlarmIntent = new Intent(SettingsActivity.this, AlarmReceiver.class);
                //whether app will vibrate once or reflect time
                mAlarmIntent.putExtra("singleVibration", mPrefs
                        .getBoolean(vibrationFrequency, false));
                mAlarmIntent.putExtra("vibrationDuration", mPrefs
                        .getInt(vibrationDuration, 100)); //passing vibration duration to alarm receiver
                mAlarmIntent.putExtra("sendNotification", mPrefs
                        .getBoolean(notificationPreference, true));
                mAlarmIntent.putExtra("shouldVibrate", mPrefs
                        .getBoolean(vibrationPreference, false));

                float intervalMinutes = Float.parseFloat(mPrefs.getString(intervalPreference, "15"));
                String intervalIntent;

                if (intervalMinutes < 60) {
                    intervalIntent = Integer.toString(Math.round(intervalMinutes)) + " minutes";
                }
                else {
                    float hours = intervalMinutes / 60;

                    if (hours == 1) {
                        intervalIntent = "1 hour";
                    }
                    else {
                        intervalIntent = Float.toString(hours) + " hours";
                    }
                }

                mAlarmIntent.putExtra("intervalUnit", intervalIntent);

                // Sets an intent which will send info to alarm receiver class, but will update an intent
                // if one already exists
                mPendingIntent = PendingIntent.getBroadcast(SettingsActivity.this, 0, mAlarmIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                // this saves battery and and creates a separate thread to manage the alarm
                AlarmManager alarmManager = (AlarmManager) getApplicationContext()
                        .getSystemService(Context.ALARM_SERVICE);

                // Setting starting time and periodic intervals of alarm
                Calendar mCalendar = Calendar.getInstance();
                mCalendar.setTimeInMillis(System.currentTimeMillis());
                // Sets offset for first instance of alarm
                int frequency = Math.round(intervalMinutes) * 60000; // in ms
                mCalendar.add(Calendar.SECOND, 0);

                // Whether app will create a wakelock (RTC_WAKEUP) or not based on user preference
                if (mPrefs.getBoolean(exactTimePreference, true)) {
                    alarmManager.setRepeating(AlarmManager.RTC, mCalendar
                            .getTimeInMillis(), frequency, mPendingIntent);
                } else {
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, mCalendar
                            .getTimeInMillis(), frequency, mPendingIntent);
                }
            }
        };
        mHandler.postAtFrontOfQueue(mRun);
    }

    public void killAlarm() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        Log.d("BenDroid/Service", "Alarm service stopped");
    }
}