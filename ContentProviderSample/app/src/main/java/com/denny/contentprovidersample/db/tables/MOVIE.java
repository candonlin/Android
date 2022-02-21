package com.denny.contentprovidersample.db.tables;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

/**
 * author:
 * created on: 2021/2/1 下午 02:22
 */
@Entity(tableName = "MOVIE", primaryKeys = {"Name"})
public class MOVIE {
    @ColumnInfo(name = "Name")
    @NonNull
    public String Name = "";

    @ColumnInfo(name = "sDate")
    @NonNull
    public String sDate = "";
}
