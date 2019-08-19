package com.naver.ers;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * used to create and load local DB
 * this class has the Dao Interface
 */
@Database(entities = {ErrorLog.class}, version = 1)
abstract class LogDatabase extends RoomDatabase {
    static final String DB_NAME = Reporter.class.getSimpleName();

    abstract ErrorLogDao errorLogDao();
}
