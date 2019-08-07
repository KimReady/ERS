package com.naver.error_reporting_sdk;

import android.app.Application;
import android.content.Context;

import com.naver.error_reporting_sdk.log.Logger;
import com.naver.error_reporting_sdk.log.LoggerFacotry;

public class Reporter {
    public static final String LOG_TAG = Reporter.class.getSimpleName();

    private static final Reporter reporter = new Reporter();
    public static Logger log = LoggerFacotry.createStub();

    public static void register(Application application) {
        Thread.UncaughtExceptionHandler defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        if(defaultHandler instanceof ErrorHandler) {
            log.w(LOG_TAG, "Reporter has been registered already.");
            return;
        }

        Thread.setDefaultUncaughtExceptionHandler(new ErrorHandler(defaultHandler));
        log = LoggerFacotry.create();
        reporter.init(application.getApplicationContext());
    }

    private ReportInfo.Builder reportInfoBuilder;

    private void init(Context context) {
        this.reportInfoBuilder = new ReportInfo.Builder(context);
    }

    public static Reporter getInstance() {
        return reporter;
    }

    public ReportInfo.Builder getReportInfo() {
        return reportInfoBuilder;
    }

    public static void crash() {
        throw new AssertionError("Test Crash");
    }
}
