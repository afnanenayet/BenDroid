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
import android.preference.SwitchPreference;
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
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Getting preference fragment and populating content of this activity with it
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

        // Starts the vibration alarm service when the activity begins
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

          // Temporary measure until a more pleasing stopping option is created
        } else if (id == R.id.stop_alarm) {
            killAlarm();
            Toast.makeText(SettingsActivity.this, "Service stopped", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences mPrefs, String key) {
            if (key.equals("is_service_enabled")) {
                Log.i("listener", key);
            }
            setAlarm();

        }
    };

    SwitchPreference sPref = (SwitchPreference) findViewById(R.id.); //TODO fix this shit
    /*SharedPreferences.OnSharedPreferenceChangeListener mListener = new
            SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                      String key) {
                    if (key == "is_service_enabled") {
                        PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
                        //TODO get value of key and either kill or start service accordingly
                    }
                    // your stuff here
                }
            };*/

    // Creates the Alarm service which specifies what times
    public void setAlarm() {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mPrefs.registerOnSharedPreferenceChangeListener(listener);

        Log.i("BenDroid/Settings", "SetAlarm called successfully");
        Intent mAlarmIntent = new Intent(SettingsActivity.this, AlarmReceiver.class);

        mAlarmIntent.putExtra("singleVibration", mPrefs.getBoolean("vibration_frequency_pref", false));
        mAlarmIntent.putExtra("vibrationDuration", 50 /*mPrefs.getInt("vibration_pref", 2)*/); //passing vibration duration to alarm receiver
        mAlarmIntent.putExtra("sendNotification", mPrefs.getBoolean("send_notification", true));

        mPendingIntent = PendingIntent.getBroadcast(SettingsActivity.this, 0, mAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Creating alarm manager to make alarm service
        //This saves battery and and creates a separate thread to manage the alarm
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        //setting starting time and periodic intervals of alarm
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        mCalendar.add(Calendar.SECOND, 0); // first time TODO: tie this to mPrefs
        long frequency = 60 * 1000; // in ms TODO tie this to mPrefs

        if (mPrefs.getBoolean("exact_time_pref", true)) {
            alarmManager.setRepeating(AlarmManager.RTC, mCalendar.getTimeInMillis(), frequency, mPendingIntent);
        }

        else {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), frequency, mPendingIntent);
        }

    }

    public void killAlarm() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        Log.i("BenDroid/Settings", "Alarm stopped successfully");
    }
}
