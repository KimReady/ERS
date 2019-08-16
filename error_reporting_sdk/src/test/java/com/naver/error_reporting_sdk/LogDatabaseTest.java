package com.naver.error_reporting_sdk;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class LogDatabaseTest {
    private Context context;
    private LogDatabase db;
    private ErrorLogDao dao;

    @Before
    public void createDb() {
        context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, LogDatabase.class).build();
        dao = db.errorLogDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void writeAndReadLog() {
        ReportInfo sampleInfo = new ReportInfo.Builder(context)
                .tag("Test tag")
                .message("Test Message")
                .build();
        ErrorLog log = new ErrorLog(sampleInfo);
        dao.insertErrorLog(log);
        List<ErrorLog> logs = dao.selectAllErrorLogs();
        assertFalse(logs.isEmpty());
    }

    @Test
    public void writeAndDeleteLog() {
        ReportInfo sampleInfo = new ReportInfo.Builder(context)
                .tag("Test tag")
                .message("Test Message")
                .build();
        ErrorLog log = new ErrorLog(sampleInfo);
        dao.insertErrorLog(log);
        dao.deleteLogs();
        List<ErrorLog> logs = dao.selectAllErrorLogs();
        assertTrue(logs.isEmpty());
    }
}