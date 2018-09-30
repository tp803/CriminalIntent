package com.bignerdranch.android.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bignerdranch.android.criminalintent.database.CrimeBaseHelper;
import com.bignerdranch.android.criminalintent.database.CrimeCursorWrapper;
import com.bignerdranch.android.criminalintent.database.CrimeDbSchema;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    // s prefix makes it clear that it is a static variable
    // private constructor means other classes can't create a CrimeLab
    private static CrimeLab sCrimeLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    //you pass in a Context object
    public static CrimeLab get(Context context){
        if (sCrimeLab == null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context){

        // create crime database
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
    }

    public void addCrime(Crime c){
        ContentValues values = getContentValues(c);

        // insert a row into database
        // first argument is the table you want to add this to
        //second argument rarely used
        // third argument is the data you want to put in
        mDatabase.insert(CrimeDbSchema.CrimeTable.NAME, null, values);

    }

    public void deleteCrime(Crime crime){
        mDatabase.delete(CrimeDbSchema.CrimeTable.NAME,
                CrimeDbSchema.CrimeTable.Cols.UUID +"=?",
                new String[]{crime.getmId().toString()});
    }


    // return mCrimes list
    public List<Crime> getmCrimes(){

        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursor = queryCrimes(null,null);

        try{
            // start at the top
            cursor.moveToFirst();
            // loop through the list
            while(!cursor.isAfterLast()){
                // add crimes
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }finally {
            // need to ensure this last task is completed
            cursor.close();
        }

        return crimes;
    }

    // find UUID and return the crime
    public Crime getCrime(UUID id){

        CrimeCursorWrapper cursor = queryCrimes(
                CrimeDbSchema.CrimeTable.Cols.UUID + "=?",
                new String[]{id.toString()}
        );

        try{
            if (cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
    }

    public File getPhotoFile(Crime crime){
        File filesDir = mContext.getFilesDir();
        return new File(filesDir,crime.getPhotoFilename());
    }

    // method to update rows in database after change
    public void updateCrime(Crime crime){
        String uuidString = crime.getmId().toString();
        ContentValues values = getContentValues(crime);
        // update is similar to insert, but need to specify which rows get updated
        mDatabase.update(CrimeDbSchema.CrimeTable.NAME, values,
                CrimeDbSchema.CrimeTable.Cols.UUID + "=?",
                // use String[] instead of just the UUID due to errors that can happen in SQL
                new String[]{uuidString});
    }

    // Querying for crimes
    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                CrimeDbSchema.CrimeTable.NAME,
                null,// columns - null selects all columns
                whereClause,
                whereArgs,
                null,//groupBy
                null,// having
                null // orderBy

        );

        return new CrimeCursorWrapper(cursor);
    }

    // Creating ContentValues
    private static ContentValues getContentValues(Crime crime){
        ContentValues values = new ContentValues();
        values.put(CrimeDbSchema.CrimeTable.Cols.UUID, crime.getmId().toString());
        values.put(CrimeDbSchema.CrimeTable.Cols.TITLE, crime.getmTitle());
        values.put(CrimeDbSchema.CrimeTable.Cols.DATE, crime.getmDate().getTime());
        values.put(CrimeDbSchema.CrimeTable.Cols.SOLVED, crime.ismSolved()?1:0);
        values.put(CrimeDbSchema.CrimeTable.Cols.SUSPECT, crime.getmSuspect());

        return values;

    }


}


