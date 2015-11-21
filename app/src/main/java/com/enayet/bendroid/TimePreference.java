package com.enayet.bendroid;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimePreference extends DialogPreference {
    protected Calendar calendar;
    protected TimePicker picker;


    public TimePreference(Context ctxt, AttributeSet attrs) {
        super(ctxt, attrs);
        setDialogLayoutResource(R.layout.time_picker);
        setPositiveButtonText(R.string.action_set);
        setNegativeButtonText(R.string.action_cancel);
        picker = new TimePicker(ctxt);
        setPersistent(false);
    }

    @Override
    protected View onCreateDialogView() {
        View view = super.onCreateDialogView();

        //picker = (TimePicker) view.findViewById(R.id.timePicker);
        picker.setIs24HourView(true);
        return view;
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);

//        picker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
  //      picker.setCurrentMinute(calendar.get(Calendar.MINUTE));
    }

    @Override
    @TargetApi(23)
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            calendar.set(Calendar.HOUR_OF_DAY, picker.getHour());
            calendar.set(Calendar.MINUTE, picker.getMinute());

            /*if (callChangeListener(calendar.getTimeInMillis())) {
                persistLong(calendar.getTimeInMillis());
                notifyChanged();
            }*/
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return (a.getString(index));
    }

    @Override
    public CharSequence getSummary() {
        if (calendar == null) {
            return null;
        }
        return DateFormat.getTimeFormat(getContext()).format(new Date(calendar.getTimeInMillis()));
    }
}
