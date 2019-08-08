package com.naver.error_reporting_sdk;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.naver.error_reporting_sdk.log.Logger;
import com.naver.error_reporting_sdk.log.LoggerFacotry;
import com.naver.error_reporting_sdk.sender.SenderService;

public final class Reporter {
    public static final String LOG_TAG = Reporter.class.getSimpleName();

    @NonNull
    public static Logger log = LoggerFacotry.createStub();

    public static void register(Application application) {
        Thread.UncaughtExceptionHandler defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        if(defaultHandler instanceof ErrorHandler) {
            log.w(LOG_TAG, "Reporter has been registered already.");
            return;
        }

        Context context = application.getApplicationContext();

        Thread.setDefaultUncaughtExceptionHandler(new ErrorHandler(defaultHandler, context));

        log = LoggerFacotry.create(context);
    }

    public static void reportError(ReportInfo reportInfo) {
        Context context = reportInfo.getContext();

        Intent intent = new Intent(context, SenderService.class);
        intent.putExtra(ReportInfo.class.getSimpleName(), reportInfo);

        ComponentName service = context.startService(intent);
        if(service == null) {
            Log.w(Reporter.LOG_TAG, "Failed to start the Sender Service.");
        }
    }

    public static void crash() {
        throw new AssertionError("Test Crash");
    }

    private Reporter(){}
}
