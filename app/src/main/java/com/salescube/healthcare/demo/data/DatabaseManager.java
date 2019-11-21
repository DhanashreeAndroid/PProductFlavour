package com.salescube.healthcare.demo.data;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by user on 19/09/2016.
 */

public class DatabaseManager {

    private final static String TAG = DatabaseManager.class.getSimpleName();
    private  int openCounter = 0;

    private static DatabaseManager instance;
    private static SQLiteOpenHelper databseHelper;
    private SQLiteDatabase database;

    public static synchronized void initializeInstance(SQLiteOpenHelper helper){
        if (instance == null){
            instance =  new DatabaseManager();
            databseHelper = helper;
        }
    }

    public static synchronized DatabaseManager getInstance(){
        if (instance == null){
            throw  new IllegalStateException(TAG + " is not initalized, call inialized() method first.");
        }
        return  instance;
    }

    public synchronized SQLiteDatabase openDatabase(){
        openCounter +=1;
        if (openCounter == 1){
            database = databseHelper.getWritableDatabase();
        }
        return  database;
    }

    public synchronized void closeDatabase(){
        openCounter  -=1;
        if (openCounter == 0){
            database.close();
        }
    }

}
