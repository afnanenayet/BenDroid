package com.enayet.bendroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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

    public void closeInit(View view) {
        switchToSettings();
    }

    public void switchToSettings() {
        Intent activityIntent = new Intent(this, SettingsActivity.class);
        startActivity(activityIntent);
        finish();
    }
}
