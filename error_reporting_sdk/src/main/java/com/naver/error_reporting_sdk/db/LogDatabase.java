package com.naver.error_reporting_sdk.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.naver.error_reporting_sdk.Reporter;

@Database(entities = {ErrorLog.class}, version = 1)
public abstract class LogDatabase extends RoomDatabase {
    public static final String DB_NAME = Reporter.class.getSimpleName();

    public abstract ErrorLogDao errorLogDao();
}
