package com.bignerdranch.android.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    // s prefix makes it clear that it is a static variable
    // private constructor means other classes can't create a CrimeLab
    private static CrimeLab sCrimeLab;

    private List<Crime> mCrimes;

    //you pass in a Context object
    public static CrimeLab get(Context context){
        if (sCrimeLab == null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context){
        mCrimes = new ArrayList<>();
        for (int i = 0; i < 100; i++){
            Crime crime = new Crime();
            crime.setmTitle("Crime #" + i);
            crime.setmSolved(i%2 == 0); // every other one
            mCrimes.add(crime);
        }

    }

    // return mCrimes list
    public List<Crime> getmCrimes(){
        return mCrimes;
    }

    // find UUID and return the crime
    public Crime getCrime(UUID id){
        for(Crime crime:mCrimes){
            if(crime.getmId().equals(id)){
                return crime;
            }
        }

        return null;
    }
}
