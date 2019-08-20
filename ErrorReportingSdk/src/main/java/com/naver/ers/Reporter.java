package com.naver.ers;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.naver.httpclientlib.CallBack;
import com.naver.httpclientlib.CallTask;
import com.naver.httpclientlib.HttpClient;
import com.naver.httpclientlib.Response;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *  the only class that the user needs to register.
 *  It doesn't provide an instance, but provide all methods statically.
 *  key roles include registration, server time checking, and error reporting.
 */
public final class Reporter {
    private static final String LOG_TAG = Reporter.class.getSimpleName();
    private static final String SERVER_URL = "http://10.66.83.42:8000";

    private static int diffTimeWithServer;
    private static AtomicBoolean hasDiffTime = new AtomicBoolean(false);
    private static AtomicBoolean isRegister = new AtomicBoolean(false);

    private static String appVersion;
    private static CustomData customData;
    private static Application application;

    @NonNull
    public static final Logger log = LoggerImpl.create();

    /**
     * register an UncaughtExceptionHandler using the user's application context
     *
     * @param application user's application
     */
    public static synchronized void register(Application application) {
        if(application == null) {
            Log.w(LOG_TAG, "application parameter must not be null.");
            return;
        }

        Thread.UncaughtExceptionHandler defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (defaultHandler instanceof ErrorHandler || isRegister.get()) {
            Log.d(LOG_TAG, "Reporter has been registered already.");
            return;
        }
        isRegister.set(true);

        Reporter.application = application;
        Thread.setDefaultUncaughtExceptionHandler(new ErrorHandler(defaultHandler, application));
        try {
            appVersion = application.getPackageManager()
                    .getPackageInfo(application.getPackageName(), 0)
                    .versionName;
        } catch(PackageManager.NameNotFoundException e) {
            Log.e(LOG_TAG, "Failed to get Version Info : " + e.getMessage());
        }

        setDifferentTimeFromServer();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            RetrieveJobScheduler.setUpdateJob(application);
        } else {
            Intent intent = new Intent(application, RetrieveLocalService.class);
            ComponentName service = application.startService(intent);
            if (service == null) {
                Log.w(LOG_TAG, "Failed to start the RetrieveLocalService.");
            }
        }
    }

    /**
     * start service to send the report
     *
     * @param reportInfo Report Information to send
     */
    static void reportError(ReportInfo reportInfo) {
        Context context = reportInfo.getContext();

        Intent intent = new Intent(context, SenderService.class);
        Bundle bundleReportInfo = reportInfo.makeBundle();
        intent.putExtra(ReportInfo.class.getSimpleName(), bundleReportInfo);

        ComponentName service = context.startService(intent);
        if (service == null) {
            Log.w(LOG_TAG, "Failed to start the Sender Service.");
        }
    }

    static int getDiffTimeWithServer() {
        return diffTimeWithServer;
    }

    static boolean hasDiffTime() {
        return hasDiffTime.get();
    }

    public static void setCustomData(CustomData customData) {
        Reporter.customData = customData;
    }

    static CustomData getCustomData() {
        return customData;
    }

    static String getAppVersion() {
        return appVersion;
    }

    static Application getApplication() {
        return application;
    }

    /**
     * get time from server
     * then calculate the difference with the current time and server time
     */
    static synchronized void setDifferentTimeFromServer() {
        if(hasDiffTime()) {
            return;
        }

        HttpClient httpClient = new HttpClient.Builder()
                .baseUrl(SERVER_URL)
                .build();
        HttpService httpService = httpClient.create(HttpService.class);

        final CallTask<ServerTime> call = httpService.getServerTime();
        call.enqueue(new CallBack<ServerTime>() {
            @Override
            public void onResponse(Response<ServerTime> response) {
                if (response.isSuccessful()) {
                    ServerTime serverTime = response.body();
                    Date serverDate = Util.toTimestamp(serverTime.getCurrentTime());
                    Date nowDate = new Date();
                    diffTimeWithServer = (int) (serverDate.getTime() - nowDate.getTime()) / 1000;
                    hasDiffTime.set(true);
                } else {
                    Log.w(LOG_TAG, "failed to get time from server");
                    hasDiffTime.set(false);
                }
            }

            @Override
            public void onFailure(IOException e) {
                Log.e(LOG_TAG, "Failed to get time from server : " + e.getMessage());
                hasDiffTime.set(false);
            }
        });
    }

    private Reporter() {
    }
}
