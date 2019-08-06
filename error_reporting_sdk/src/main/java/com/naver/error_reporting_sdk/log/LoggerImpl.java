package com.naver.error_reporting_sdk.log;

import android.util.Log;

import com.naver.error_reporting_sdk.ReportInfo;

class LoggerImpl implements Logger {
    private ReportInfo info;

    LoggerImpl(ReportInfo info) {
        this.info = info;
    }

    @Override
    public int v(String tag, String msg) {
        writeLog(LogLevel.VERBOSE, tag, msg);

        return Log.v(tag, msg);
    }

    @Override
    public int v(String tag, String msg, Throwable tr) {
        writeLog(LogLevel.VERBOSE, tag, msg, tr);

        return Log.v(tag, msg, tr);
    }

    @Override
    public int d(String tag, String msg) {
        writeLog(LogLevel.DEBUG, tag, msg);

        return Log.d(tag, msg);
    }

    @Override
    public int d(String tag, String msg, Throwable tr) {
        writeLog(LogLevel.DEBUG, tag, msg, tr);

        return Log.d(tag, msg, tr);
    }

    @Override
    public int i(String tag, String msg) {
        writeLog(LogLevel.INFO, tag, msg);

        return Log.i(tag, msg);
    }

    @Override
    public int i(String tag, String msg, Throwable tr) {
        writeLog(LogLevel.INFO, tag, msg, tr);

        return Log.i(tag, msg, tr);
    }

    @Override
    public int w(String tag, String msg) {
        writeLog(LogLevel.WARNING, tag, msg);

        return Log.w(tag, msg);
    }

    @Override
    public int w(String tag, String msg, Throwable tr) {
        writeLog(LogLevel.WARNING, tag, msg, tr);

        return Log.w(tag, msg, tr);
    }

    @Override
    public int e(String tag, String msg) {
        writeLog(LogLevel.ERROR, tag, msg);

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

    private synchronized void writeLog(LogLevel level, String tag, String msg, Throwable tr) {
        StringBuilder builder = new StringBuilder(msg);
        builder.append("\n");
        builder.append(getStackTrace(tr));
        writeLog(level, tag, builder.toString());
    }

    private synchronized void writeLog(LogLevel level, String tag, String msg) {
        info.put(level.getName(), tag + msg);
    }
}
