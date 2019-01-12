package com.example.ayabeltran.conitracking;

import android.app.Application;

public class DBApp extends Application {

    private DBHelper mydb;

    @Override
    public void onCreate() {
        super.onCreate();
        mydb = new DBHelper(this);

    }

    public DBHelper getDatabase(){
        return mydb;

    }
}
