package com.enayet.bendroid;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * This is a custom preference that creates a dialog with a slider that returns an integer value
 * between 0-100
 * Created by aenayet on 8/30/15.
 */
public class numberSeekerPreference extends DialogPreference implements SeekBar.OnSeekBarChangeListener {
    int mCurrentValue = 50;

    // Required constructor
    public numberSeekerPreference(Context context, AttributeSet attributes) {
        super(context, attributes);

        setDialogLayoutResource(R.layout.number_seeker_layout);

        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);

        // Setting to false because we are storing the value ourselves
        setPersistent(false);

        setDialogIcon(null);
    }

    @Override
    // Function sets whether activity preference will persist value
    public void setPersistent(boolean persistent) {
        super.setPersistent(persistent);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {

        if (restorePersistedValue) {
            Log.i("seekbarPref", "Restoring persisted value");
            // Restore existing state
            mCurrentValue = this.getPersistedInt(R.id.seekBarPref);
        } else {
            // Set default state from the XML attribute
            Log.i("seekbarPref", "Setting default value");
            mCurrentValue = (Integer) defaultValue;
            persistInt(mCurrentValue);
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        Log.i("seekbarPref", "Getting default value");
        return a.getInteger(index, DEFAULT_ORDER);
    }


    @Override
    protected void onDialogClosed(boolean positiveResult) {
        // When the user selects "OK", persist the new value
        if (positiveResult) {
            Log.i("seekerPreference", "persistInt method called");
            persistInt(R.id.seekBarPref); //gets value of seekbarpreference
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // TODO add something here
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        TextView mTextView;

    }
}
