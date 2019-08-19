package com.naver.ers;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Concrete class of the {@link Logger}
 * if Context is registered in Reporter, user can send the log to the server
 * if not, user just can write the log using default Log
 */
class LoggerImpl implements Logger {

    static Logger create() {
        return new LoggerImpl();
    }

    private LoggerImpl() { }

    @Override
    public int v(String tag, String msg) {
        if(Reporter.getContext() != null) {
            writeLog(LogLevel.VERBOSE, tag, msg, null);
        }
        return Log.v(tag, msg);
    }

    @Override
    public int v(String tag, String msg, Throwable tr) {
        if(Reporter.getContext() != null) {
            writeLog(LogLevel.VERBOSE, tag, msg, tr);
        }
        return Log.v(tag, msg, tr);
    }

    @Override
    public int d(String tag, String msg) {
        if(Reporter.getContext() != null) {
            writeLog(LogLevel.DEBUG, tag, msg, null);
        }
        return Log.d(tag, msg);
    }

    @Override
    public int d(String tag, String msg, Throwable tr) {
        if(Reporter.getContext() != null) {
            writeLog(LogLevel.DEBUG, tag, msg, tr);
        }
        return Log.d(tag, msg, tr);
    }

    @Override
    public int i(String tag, String msg) {
        if(Reporter.getContext() != null) {
            writeLog(LogLevel.INFO, tag, msg, null);
        }
        return Log.i(tag, msg);
    }

    @Override
    public int i(String tag, String msg, Throwable tr) {
        if(Reporter.getContext() != null) {
            writeLog(LogLevel.INFO, tag, msg, tr);
        }
        return Log.i(tag, msg, tr);
    }

    @Override
    public int w(String tag, String msg) {
        if(Reporter.getContext() != null) {
            writeLog(LogLevel.WARNING, tag, msg, null);
        }
        return Log.w(tag, msg);
    }

    @Override
    public int w(String tag, String msg, Throwable tr) {
        if(Reporter.getContext() != null) {
            writeLog(LogLevel.WARNING, tag, msg, tr);
        }
        return Log.w(tag, msg, tr);
    }

    @Override
    public int e(String tag, String msg) {
        if(Reporter.getContext() != null) {
            writeLog(LogLevel.ERROR, tag, msg, null);
        }
        return Log.e(tag, msg);
    }

    @Override
    public int e(String tag, String msg, Throwable tr) {
        if(Reporter.getContext() != null) {
            writeLog(LogLevel.ERROR, tag, msg, tr);
        }
        return Log.e(tag, msg, tr);
    }

    @Override
    public String getStackTrace(Throwable tr) {
        return Log.getStackTraceString(tr);
    }

    /**
     * after building the custom log, call {@link Reporter#reportError(ReportInfo)}
     * so that send it to the server
     */
    private void writeLog(@NonNull LogLevel level,
                          @NonNull String tag,
                          @NonNull String msg,
                          @Nullable Throwable tr) {
        String stackTrace = Util.parseString(tr);
        ReportInfo reportInfo = new ReportInfo.Builder(Reporter.getContext())
                .logLevel(level.name())
                .tag(tag)
                .message(msg)
                .stackTrace(stackTrace)
                .build();
        Reporter.reportError(reportInfo);
    }

}
