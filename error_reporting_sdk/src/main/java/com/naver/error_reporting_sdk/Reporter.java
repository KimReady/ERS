package com.naver.error_reporting_sdk;

import android.app.Application;

import com.naver.error_reporting_sdk.log.Logger;
import com.naver.error_reporting_sdk.log.LoggerFacotry;

public class Reporter {
    static final String LOG_TAG = Reporter.class.getSimpleName();

    public static Logger log = LoggerFacotry.createStub();


    public static void register(Application application) {
        Thread.UncaughtExceptionHandler defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        if(defaultHandler instanceof ErrorHandler) {
            log.w(LOG_TAG, "Reporter has been registered already.");
            return;
        }

        Thread.setDefaultUncaughtExceptionHandler(new ErrorHandler(defaultHandler));
        log = LoggerFacotry.create();
    }

}
