package com.bignerdranch.android.criminalintent.database;

public class CrimeDbSchema {
    // table to define the string constants needed to describe the moving pieces of your table def
    public static final class CrimeTable{
        public static final String NAME = "crimes";

        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
            public static final String SUSPECT = "suspect";
        }
    }
}
