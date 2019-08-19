package com.naver.ers;

import android.content.Context;
import android.util.Log;

import com.naver.error_reporting_sdk.R;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * handler class for handling the UncaughtException
 * ErrorHandler holds the defaultExceptionHandler,
 * proceed using it after sending the error log
 */
class ErrorHandler implements Thread.UncaughtExceptionHandler {
    private static final String LOG_TAG = ErrorHandler.class.getSimpleName();

    private Thread.UncaughtExceptionHandler defaultHandler;
    private AtomicBoolean handled;
    private Context context;

    ErrorHandler(Thread.UncaughtExceptionHandler defaultHandler, Context context) {
        this.defaultHandler = defaultHandler;
        this.context = context;
        this.handled = new AtomicBoolean(false);
    }

    /**
     * called when an UncaughtException occurs
     * after handling the error log, kill the process and application
     *
     * @param t Exception thread
     * @param e Exception
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        synchronized (this) {
            if (handled.get()) {
                Log.w(LOG_TAG, "ExceptionHandler has been already executed.");
                return;
            }
            handled.set(true);
        }

        try {
            String stackTrace = Util.parseString(e);

            ReportInfo reportInfo = new ReportInfo.Builder(context)
                    .tag(context.getResources().getString(R.string.uncaught_exception_tag))
                    .message(e != null ? e.toString() : "")
                    .stackTrace(stackTrace)
                    .build();

            Reporter.reportError(reportInfo);
        } catch(Exception tr) {
            Reporter.log.e(LOG_TAG, "Error has occurred while reporting exception. : " + tr.getMessage());
        } finally {
            defaultHandler.uncaughtException(t, e);
        }
    }
}
