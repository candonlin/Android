package com.denny.bigdatasample.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.denny.bigdatasample.db.dao.MovieDAO;
import com.denny.bigdatasample.db.table.MOVIE;

/**
 * author:
 * created on: 2021/2/1 下午 02:22
 */
@Database(entities = {
        MOVIE.class,
}, version = 1)
public abstract class AppDataBase extends RoomDatabase {
    public static final String DBNAME = "Test.db";
    private static AppDataBase INSTANCE;

    public abstract MovieDAO movieDAO();

    public static AppDataBase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room
                    .databaseBuilder(context,
                            AppDataBase.class,
                            AppDataBase.DBNAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
