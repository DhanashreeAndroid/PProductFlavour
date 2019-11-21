package com.salescube.healthcare.demo.data.repo;

import android.database.Cursor;

import com.salescube.healthcare.demo.data.DatabaseManager;

/**
 * Created by user on 01/10/2016.
 */

public abstract class RepoBase {

    protected RepoBase() {

    }

    protected void closeCursor(Cursor c) {
        if (c != null) {
            c.close();
        }
        DatabaseManager.getInstance().closeDatabase();
    }
//    public static class Crs {
//        public static int getInt(Cursor c, String columnName) {
//            int ix = c.getColumnIndexOrThrow(columnName);
//            int result = c.getInt(ix);
//            return result;
//        }
//
//        public static String getString(Cursor c, String columnName) {
//            int ix = c.getColumnIndexOrThrow(columnName);
//            String result = c.getString(ix);
//            return result;
//        }
//    }

}
