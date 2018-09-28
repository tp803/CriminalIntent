package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {

    public static final String EXTRA_DATE = "com.bignerdrange.android.criminalintent.date";

    private static final String ARG_DATE = "date";

    private DatePicker mDatePicker;

    // newInstance(Date) method
    public static DatePickerFragment newInstance(Date date){
        // creates a new bundle "args"
        Bundle args = new Bundle();
        // put date in args
        args.putSerializable(ARG_DATE, date);

        // create new fragment
        DatePickerFragment fragment = new DatePickerFragment();
        // set arguments with date
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Date date = (Date)getArguments().getSerializable(ARG_DATE);

        // put year, month, and day into integers
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_date,null);

        // sets the year, month and day that datepicker should have
        mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_picker);
        mDatePicker.init(year, month, day, null);

        // pass a Context into the AlertDialog.Builder constructor
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                // accepts a string resource and an object that implements
                // DialogInterface.OnClickListener
                // positive button accepts waht the dialog represents
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int year = mDatePicker.getYear();
                                int month = mDatePicker.getMonth();
                                int day = mDatePicker.getDayOfMonth();

                                Date date = new GregorianCalendar(year, month, day).getTime();
                                // uses private method created below
                                sendResult(Activity.RESULT_OK, date);
                            }
                        })
                .create();
    }

    // private method that creates an intent, puts the date on it as an extra, then calls
    // CrimeFragment.onActivityResult(...)
    private void sendResult(int resultCode, Date date){
        if(getTargetFragment() == null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(),resultCode,intent);
    }

}
