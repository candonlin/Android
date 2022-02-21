package com.denny.bigdatasample.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.denny.bigdatasample.db.table.MOVIE;

import java.util.ArrayList;
import java.util.List;

/**
 * author: Denny
 * created on: 2021/1/26 下午 03:41
 */
@Dao
public interface MovieDAO {
    @Query("SELECT * FROM MOVIE")
    List<MOVIE> GetRecords();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecord(MOVIE movie);

    @Insert
    void insertRecords(ArrayList<MOVIE> list);

    @Update
    void UpdateRecord(MOVIE scrts);

    @Query("DELETE FROM MOVIE")
    void Delete();

    @Query("SELECT * FROM MOVIE")
    LiveData<List<MOVIE>> getStudentList();//若需要監聽表的變化，為其加上LiveData


}

