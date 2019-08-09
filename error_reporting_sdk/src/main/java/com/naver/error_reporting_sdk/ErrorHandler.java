package com.naver.error_reporting_sdk;

import android.content.Context;
import android.util.Log;

class ErrorHandler implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler defaultHandler;
    private boolean handled;
    private Context context;

    ErrorHandler(Thread.UncaughtExceptionHandler defaultHandler, Context context) {
        this.defaultHandler = defaultHandler;
        this.context = context;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        try {
            if(handled) {
                Log.w(Reporter.LOG_TAG, "ExceptionHandler has been already executed.");
                return;
            }
            handled = true;

            ReportInfo reportInfo = new ReportInfo.Builder(context)
                    .message(e.toString())
                    .topOfStackTrace(e.getStackTrace()[0].toString())
                    .build();

            Reporter.reportError(reportInfo);
        } catch(Exception tr) {
            Reporter.log.e(Reporter.LOG_TAG, "Error has occurred while reporting exception.\n" + tr.getMessage());
        } finally {
            defaultHandler.uncaughtException(t, e);
        }
    }
}
