package com.naver.error_reporting_sdk;

public class ErrorHandler implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler defaultHandler;
    private boolean handled;

    ErrorHandler(Thread.UncaughtExceptionHandler defaultHandler) {
        this.defaultHandler = defaultHandler;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        try {
            if(handled) {
                Reporter.log.w(Reporter.LOG_TAG, "ExceptionHandler has been already executed.");
                return;
            }
            handled = true;

            Reporter.log.e(Reporter.LOG_TAG, e.getMessage());
        } catch(Throwable tr) {
            Reporter.log.e(Reporter.LOG_TAG, "Error has occurred while reporting exception.");
            defaultHandler.uncaughtException(t, e);
        }
    }
}
