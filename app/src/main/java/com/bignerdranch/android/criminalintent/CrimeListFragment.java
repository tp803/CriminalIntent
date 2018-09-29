package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.PluralsRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CrimeListFragment extends Fragment {

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private boolean mSubtitlteVisible;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                      Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_crime_list,container,false);

        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        // need to assign a recyler view a layout manager, or it will crash
        // layout manager positions every item and determines scrolling
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (savedInstanceState != null){
            mSubtitlteVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();

        return view;


        }

        // override onResume to trigger a call to updateUI
        @Override
        public void onResume(){
        super.onResume();
        updateUI();
        }

        @Override
        public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitlteVisible);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.fragment_crime_list,menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitlteVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
        }

        // respond to selection of the MenuItem by creating a new Crime, adding it to CrimeLab,
        // and then starting a new instance of CrimePagerActivity to edit the new Crime
        @Override
        public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent = CrimePagerActivity
                        .newIntent(getActivity(),crime.getmId());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                mSubtitlteVisible = !mSubtitlteVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        }

        // setting up method to update subtitle
    private void updateSubtitle(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getmCrimes().size();
        String subtitle = getString(R.string.subtitle_format, crimeCount);

        if(!mSubtitlteVisible){
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

        // update user interface
        private void updateUI(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getmCrimes();

        if(mAdapter == null) {

            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        }else{
            // if adapter is already set up, set crimes and call notifyData...
            mAdapter.setmCrimes(crimes);
            mAdapter.notifyDataSetChanged();
        }

        updateSubtitle();
        }

        // CrimeHolder constructor
        // CrimeHolder implements on click listener
        private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            private Crime mCrime;
            private TextView mTitleTextView;
            private TextView mDateTextView;
            private ImageView mSolvedImageView;

        // inflate list_item_crime.xml
        public CrimeHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_crime,parent,false));

            // on click listener
            itemView.setOnClickListener(this);

            // We only need to do this once, so we inflate here and will bind later
            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_date);
            mSolvedImageView = (ImageView) itemView.findViewById(R.id.crime_solved);
        }

        // now we bind here instead of directly after inflating.  This makes the code more flexible
        // we will have this as a reusable function
        public void bind(Crime crime){
            mCrime = crime;
            mTitleTextView.setText(mCrime.getmTitle());
            mDateTextView.setText(mCrime.getmDate().toString());
            // image is only is visible if crime.ismSolved is true
            mSolvedImageView.setVisibility(crime.ismSolved()?View.VISIBLE:View.GONE);
        }

            @Override
            public void onClick(View view){
                // when clicked, start an instance of CrimePagerActivity and pass ID
                Intent intent = CrimePagerActivity.newIntent(getActivity(),mCrime.getmId());
                startActivity(intent);
            }

        }

        // this will assist RecyclerView so it knows about the Crime object
        private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{
        private List<Crime> mCrimes;
        public CrimeAdapter(List<Crime> crimes){
            mCrimes = crimes;
        }

        //@NonNull
        @Override
        // this is done when recycler view needs to create a viewholder to display an item with
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CrimeHolder(layoutInflater, parent);
        }

        @Override
        // we need to keep this as small as possible to make it scroll smoothly
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            // this is where we call the bind function
            holder.bind(crime);

        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        public void setmCrimes(List<Crime> crimes){
            mCrimes = crimes;
        }


    }




}
