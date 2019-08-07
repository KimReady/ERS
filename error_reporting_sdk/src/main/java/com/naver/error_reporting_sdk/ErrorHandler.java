package com.naver.error_reporting_sdk;

import com.naver.error_reporting_sdk.sender.DBSender;

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

            Reporter.log.e(Reporter.LOG_TAG, e.getMessage(), e);
            ReportInfo reportInfo = Reporter.getInstance()
                    .getReportInfo()
                    .message(e.toString())
                    .topOfStackTrace(e.getStackTrace()[0].toString())
                    .build();
            reportInfo.execute(new DBSender());
        } catch(Exception tr) {
            tr.printStackTrace();
            Reporter.log.e(Reporter.LOG_TAG, "Error has occurred while reporting exception.\n" + tr.getMessage());
        } finally {
            defaultHandler.uncaughtException(t, e);
        }
    }
}
