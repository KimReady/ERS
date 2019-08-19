package com.naver.ers;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * Interface for queries or statements to be used for Room(Local DB)
 */
@Dao
interface ErrorLogDao {
    /**
     * insert a object of ErrorLog
     */
    @Insert
    void insertErrorLog(ErrorLog errorLog);

    /**
     * insert many of ErrorLog
     */
    @Insert
    void insertErrorLogs(List<ErrorLog> errorLog);

    /**
     * read all of ErrorLogs
     */
    @Query("select * from error_log")
    List<ErrorLog> selectAllErrorLogs();

    /**
     * remove all of ErrorLogs
     */
    @Query("delete from error_log")
    void deleteLogs();
}
