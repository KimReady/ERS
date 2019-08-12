package com.naver.error_reporting_sdk.log;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.naver.error_reporting_sdk.ReportInfo;
import com.naver.error_reporting_sdk.Reporter;

import java.io.PrintWriter;
import java.io.StringWriter;

class LoggerImpl implements Logger {
    private final Context context;

    LoggerImpl(Context context) {
        this.context = context;
    }

    @Override
    public int v(String tag, String msg) {
        writeLog(LogLevel.VERBOSE, tag, msg, null);

        return Log.v(tag, msg);
    }

    @Override
    public int v(String tag, String msg, Throwable tr) {
        writeLog(LogLevel.VERBOSE, tag, msg, tr);

        return Log.v(tag, msg, tr);
    }

    @Override
    public int d(String tag, String msg) {
        writeLog(LogLevel.DEBUG, tag, msg, null);

        return Log.d(tag, msg);
    }

    @Override
    public int d(String tag, String msg, Throwable tr) {
        writeLog(LogLevel.DEBUG, tag, msg, tr);

        return Log.d(tag, msg, tr);
    }

    @Override
    public int i(String tag, String msg) {
        writeLog(LogLevel.INFO, tag, msg, null);

        return Log.i(tag, msg);
    }

    @Override
    public int i(String tag, String msg, Throwable tr) {
        writeLog(LogLevel.INFO, tag, msg, tr);

        return Log.i(tag, msg, tr);
    }

    @Override
    public int w(String tag, String msg) {
        writeLog(LogLevel.WARNING, tag, msg, null);

        return Log.w(tag, msg);
    }

    @Override
    public int w(String tag, String msg, Throwable tr) {
        writeLog(LogLevel.WARNING, tag, msg, tr);

        return Log.w(tag, msg, tr);
    }

    @Override
    public int e(String tag, String msg) {
        writeLog(LogLevel.ERROR, tag, msg, null);

        return Log.e(tag, msg);
    }

    @Override
    public int e(String tag, String msg, Throwable tr) {
        writeLog(LogLevel.ERROR, tag, msg, tr);

        return Log.e(tag, msg, tr);
    }

    @Override
    public String getStackTrace(Throwable tr) {
        return Log.getStackTraceString(tr);
    }

    private void writeLog(@NonNull LogLevel level,
                          @NonNull String tag,
                          @NonNull String msg,
                          @Nullable Throwable tr) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        if(tr != null) {
            tr.printStackTrace(pw);
        }
        String stackTrace = tr != null ? sw.toString() : "";
        ReportInfo reportInfo = new ReportInfo.Builder(context)
                .logLevel(level)
                .message(tag+": "+msg)
                .stackTrace(stackTrace)
                .build();
        Reporter.reportError(reportInfo);
    }

}
