package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";

    // constant for the request code
    private static final int REQUEST_DATE = 0;

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;

    // method that saccepts a UUID
    public static CrimeFragment newInstance(UUID crimeId){
        // creates arguments bundle
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        // creates fragment instance
        CrimeFragment fragment = new CrimeFragment();
        // attaches argument to the fragment
        fragment.setArguments(args);
        return fragment;
    }

    // start this up when created.  This must be public as it's called by the hosting activity
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // retreive UUID from fragment arguments
        UUID crimeId = (UUID)getArguments().getSerializable(ARG_CRIME_ID);
        // fetch mCrime
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    // When CrimeFragement is done, modifications need to be written out
    @Override
    public void onPause(){
        super.onPause();

        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    // This is where we inflate the view from fragment_crime.xml
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        // wire up box for text entry
        mTitleField = (EditText)v.findViewById(R.id.crime_title);
        // set to whatever the title is
        mTitleField.setText(mCrime.getmTitle());
        // watch for text entry
        mTitleField.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after){
                // do nothing before text is changed

            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count){
                // when text is entered, convert to string and set to title
                mCrime.setmTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s){
                // do nothing after text is changed

            }
        });

        // wire up button
        mDateButton = (Button) v.findViewById(R.id.crime_date);
        // set text in button to today's date
        updateDate();
        //
        mDateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // sets fragment manager as manager
                FragmentManager manager = getFragmentManager();
                // call to DatePickerFragmenet.newInstance to create new fragment with date
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getmDate());
                // make CrimeFragment the target fragment of the DatePickerFragment instance
                dialog.setTargetFragment(CrimeFragment.this,REQUEST_DATE);
                // DIALOG_DATE is a constant for the tag
                dialog.show(manager, DIALOG_DATE);
            }
        });

        // wire up check box
        mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
        // set checkbox to clicked if solved
        mSolvedCheckBox.setChecked(mCrime.ismSolved());
        // set a listener.  Change mSolved if checked
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setmSolved(isChecked);
            }
        });

        // return the view
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_DATE){
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setmDate(date);
            updateDate();
        }
    }

    private void updateDate() {
        mDateButton.setText(mCrime.getmDate().toString());
    }

}
