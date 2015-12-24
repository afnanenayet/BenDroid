package com.enayet.bendroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    final String firstView = "welcomeScreenShown";
    SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean initialView = mPrefs.getBoolean(firstView, true);

        if (!initialView) {
            switchToSettings();
        }

        else {
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putBoolean(firstView, false);
            editor.apply();
            setContentView(R.layout.activity_main);
        }
    }

    public void switchToSettings() {
        Intent activityIntent = new Intent(this, SettingsActivity.class); // activity transaction with intent
            startActivity(activityIntent);
        finish(); // destroying current activity to free memory (we're not using this anymore)
    }
}
