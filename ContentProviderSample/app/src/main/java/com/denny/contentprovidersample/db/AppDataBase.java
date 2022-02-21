package com.denny.contentprovidersample.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.denny.contentprovidersample.db.tables.MOVIE;

/**
 * author: Denny
 * created on: 2021/2/3 上午 09:55
 */

@Database(entities = {
        MOVIE.class,}, version = 1)
public abstract class AppDataBase extends RoomDatabase {
    public static final String DBNAME = "RegalSample.db";

    private static AppDataBase INSTANCE;

    public static AppDataBase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room
                    .databaseBuilder(context,
                            AppDataBase.class,
                            AppDataBase.DBNAME)
                    .fallbackToDestructiveMigration()
//                    .addMigrations(MIGRATION_1_2)
                    .build();
        }
        return INSTANCE;
    }

//    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            database.execSQL("ALTER TABLE Repo "
//                    + "ADD COLUMN html_url TEXT");
//        }
//    };

}