package com.naver.error_reporting_sdk;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ErrorLog.class}, version = 1)
abstract class LogDatabase extends RoomDatabase {
    static final String DB_NAME = Reporter.class.getSimpleName();

    abstract ErrorLogDao errorLogDao();
}
