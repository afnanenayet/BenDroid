package com.enayet.bendroid;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by aenayet on 8/30/15.
 */
public class numberSeekerPreference extends DialogPreference {
    int mCurrentValue;

    public numberSeekerPreference(Context context, AttributeSet attributes) {
        super(context, attributes);
        setDialogLayoutResource(R.layout.number_seeker_layout);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
        setDialogIcon(null);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {

        if (restorePersistedValue) {
            // Restore existing state
            mCurrentValue = this.getPersistedInt(R.id.seekBarPref);
        } else {
            // Set default state from the XML attribute
            mCurrentValue = (Integer) defaultValue;
            persistInt(mCurrentValue);
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index, DEFAULT_ORDER);
    }

    /*@Override
    protected void onCreateDialog() {
        //TODO: figure out how to bind TextView to value of seekbar
    }*/

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        // When the user selects "OK", persist the new value
        if (positiveResult) {
            persistInt(R.id.seekBarPref); //gets value of seekbarpreference
        }
    }
}
