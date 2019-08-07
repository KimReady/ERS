package com.naver.error_reporting_sdk.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ErrorLog.class}, version = 1)
public abstract class LogDatabase extends RoomDatabase {
    public abstract ErrorLogDao errorLogDao();
}
