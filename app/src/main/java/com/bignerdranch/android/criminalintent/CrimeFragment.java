package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.UUID;

public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";

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
        mDateButton.setText(mCrime.getmDate().toString());
        // disable button for now
        mDateButton.setEnabled(false);

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


}
