package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();

    @LayoutRes
    protected int getLayoutResId(){
        return R.layout.activity_fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(getLayoutResId());

        FragmentManager fm = getSupportFragmentManager();
        // retrieve the CrimeFragment by view ID
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        // if there is no fragment with the given ID, create a new CrimeFragment
        if (fragment == null){
            fragment = createFragment();
            // fragment transaction adds fragments in the fragment list
            fm.beginTransaction().add(R.id.fragment_container,fragment).commit();
        }
    }
}
