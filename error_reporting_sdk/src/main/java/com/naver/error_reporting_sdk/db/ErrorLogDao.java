package com.naver.error_reporting_sdk.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ErrorLogDao {
    @Insert
    void insertErrorLogs(ErrorLog... errorLogs);

    @Query("select * from error_log")
    List<ErrorLog> selectAllErrorLogs();
}
