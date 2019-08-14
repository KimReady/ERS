package com.naver.error_reporting_sdk;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
interface ErrorLogDao {
    @Insert
    void insertErrorLog(ErrorLog errorLog);

    @Insert
    void insertErrorLogs(List<ErrorLog> errorLog);

    @Query("select * from error_log")
    List<ErrorLog> selectAllErrorLogs();

    @Query("delete from error_log")
    void deleteLogs();
}
