package com.denny.contentprovidersample;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.OnConflictStrategy;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;

import com.denny.contentprovidersample.db.AppDataBase;

public class MyContentProvider extends ContentProvider {
    public static SupportSQLiteDatabase sdb = null;
    private String dbPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();

    public static final String AUTOHORITY = "com.denny.contentprovidersample";

    public static final int MOVIE_CODE = 1;
    private static final UriMatcher mMatcher;

    static {
        mMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // 初始化
        mMatcher.addURI(AUTOHORITY, "MOVIE", MOVIE_CODE);
    }

    @Override
    public boolean onCreate() {
        final AppDataBase dataBase = AppDataBase.getDatabase(getContext());
        sdb = dataBase.getOpenHelper().getWritableDatabase();
        return true;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {

        String table = getTableName(uri);
        Log.e("myResultjoin", "insert：" + table);
        sdb.insert(table, OnConflictStrategy.REPLACE, values);
        return uri;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        String table = getTableName(uri);

        //room
        sdb.beginTransaction();
        int count = values.length;
        int process=0;
        try {
            for (int i = 0; i < count; i++) {
                if (sdb.insert(table, OnConflictStrategy.REPLACE, values[i]) > 0) {
                    Log.d("bulkInsert", values[i].toString());
                    process++;
                } else {
                    Log.d("bulkInsert", "資料新增失敗");
                }
            }
            sdb.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sdb.endTransaction();
        }

        return process;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        String table = getTableName(uri);
        Log.e("myResultjoin", "query：" + table);
        //room
        SupportSQLiteQuery query = SupportSQLiteQueryBuilder.builder(table)
                .columns(projection)
                .selection(selection, selectionArgs)
                .orderBy(sortOrder)
                .create();

        return sdb.query(query);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        String table = getTableName(uri);
        int row = -1;
        try {
            //room
            row = sdb.update(table, OnConflictStrategy.REPLACE, values, selection, selectionArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return row;
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String table = getTableName(uri);
        int row = -1;
        try {
            //room
            row = sdb.delete(table, selection, selectionArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return row;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    /*** 根據URI匹配 URI_CODE，從而匹配ContentProvider中相應的表名 ***/
    public String getTableName(Uri uri) {
        String tableName = null;
        switch (mMatcher.match(uri)) {
            case MOVIE_CODE:
                tableName = "MOVIE";
                break;
            default:
                Log.d("URI ERROR", "URI錯誤");
                break;
        }
        return tableName;
    }
}
