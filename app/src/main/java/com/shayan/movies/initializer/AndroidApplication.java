package com.shayan.movies.initializer;

import android.app.Application;

import com.shayan.movies.localDB.FavoriteRoomDatabase;

public class AndroidApplication extends Application {

    private static FavoriteRoomDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();

        db = FavoriteRoomDatabase.getDatabase(this);
    }

    public static FavoriteRoomDatabase getDb() {
        return db;
    }
}
