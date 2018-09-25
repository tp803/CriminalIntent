package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CrimeListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                      Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_crime_list,container,false);

        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        // need to assign a recyler view a layout manager, or it will crash
        // layout manager positions every item and determines scrolling
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;


        }

        // override onResume to trigger a call to updateUI
        @Override
        public void onResume(){
        super.onResume();
        updateUI();
        }

        // update user interface
        private void updateUI(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getmCrimes();

        if(mAdapter == null) {

            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        }else{
            // if adapter is already set up, call notifyData...
            mAdapter.notifyDataSetChanged();
        }
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


    }




}
