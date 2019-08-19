package com.naver.ers;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ReportInfoTest {
    private Context context;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
    }

    @Test
    public void packageName() {
        ReportInfo reportInfo = new ReportInfo.Builder(context).build();
        assertNotNull(reportInfo.getPackageName());
        System.out.println(reportInfo.getPackageName());
    }

    @Test
    public void logLevel() {
        ReportInfo reportInfo = new ReportInfo.Builder(context)
                .logLevel(LogLevel.INFO.name())
                .build();
        assertEquals("INFO", reportInfo.getLogLevel());
    }

    @Test
    public void Tag() {
        ReportInfo reportInfo = new ReportInfo.Builder(context)
                .tag("Test Tag")
                .build();
        assertEquals("Test Tag", reportInfo.getTag());
    }

    @Test
    public void message() {
        ReportInfo reportInfo = new ReportInfo.Builder(context)
                .message("Test Message")
                .build();
        assertEquals("Test Message", reportInfo.getMessage());
    }

    @Test
    public void context() {
        ReportInfo reportInfo = new ReportInfo.Builder(context)
                .build();
        assertEquals(reportInfo.getContext(), context);
    }

    @Test
    public void stacktrace() {
        String stacktrace = new Throwable("Test").getStackTrace()[0].toString();
        ReportInfo reportInfo = new ReportInfo.Builder(context)
                .stackTrace(stacktrace)
                .build();
        assertEquals(reportInfo.getStackTrace(), stacktrace);
    }

}