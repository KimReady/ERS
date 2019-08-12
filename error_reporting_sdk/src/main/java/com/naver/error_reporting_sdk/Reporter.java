package com.naver.error_reporting_sdk;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.naver.error_reporting_sdk.db.ServerTime;
import com.naver.error_reporting_sdk.db.TimestampConverter;
import com.naver.error_reporting_sdk.log.Logger;
import com.naver.error_reporting_sdk.log.LoggerFactory;
import com.naver.error_reporting_sdk.sender.HttpService;
import com.naver.error_reporting_sdk.sender.SenderService;
import com.naver.httpclientlib.CallBack;
import com.naver.httpclientlib.CallTask;
import com.naver.httpclientlib.HttpClient;
import com.naver.httpclientlib.Response;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public final class Reporter {
    private static final String LOG_TAG = Reporter.class.getSimpleName();

    private static int diffTimeWithServer;
    private static AtomicBoolean hasDiffTime = new AtomicBoolean(false);

    private static UserInfo userInfo;

    @NonNull
    public static Logger log = LoggerFactory.createStub();

    public static void register(Application application) {
        Thread.UncaughtExceptionHandler defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (defaultHandler instanceof ErrorHandler) {
            log.w(LOG_TAG, "Reporter has been registered already.");
            return;
        }

        Context context = application.getApplicationContext();

        Thread.setDefaultUncaughtExceptionHandler(new ErrorHandler(defaultHandler, context));

        log = LoggerFactory.create(context);
        setDifferentTimeFromServer(context);
    }

    public static void reportError(ReportInfo reportInfo) {
        Context context = reportInfo.getContext();

        Intent intent = new Intent(context, SenderService.class);
        Bundle bundleReportInfo = reportInfo.makeBundle();
        intent.putExtra(ReportInfo.class.getSimpleName(), bundleReportInfo);

        ComponentName service = context.startService(intent);
        if (service == null) {
            Log.w(LOG_TAG, "Failed to start the Sender Service.");
        }
    }

    public static void crash() {
        throw new AssertionError("Test Crash");
    }

    public static int getDiffTimeWithServer() {
        return diffTimeWithServer;
    }

    public static boolean hasDiffTime() {
        return hasDiffTime.get();
    }

    public static void setUserInfo(UserInfo.Builder userInfoBuilder) {
        userInfo = userInfoBuilder.build();
    }

    public static UserInfo getUserInfo() {
        return userInfo;
    }

    public static void setDifferentTimeFromServer(Context context) {
        HttpClient httpClient = new HttpClient.Builder()
                .baseUrl(context.getResources().getString(R.string.server_url))
                .build();
        HttpService httpService = httpClient.create(HttpService.class);

        final CallTask<ServerTime> call = httpService.getServerTimeWithDynamicURL();
        call.enqueue(new CallBack<ServerTime>() {
            @Override
            public void onResponse(Response<ServerTime> response) throws IOException {
                if (response.isSuccessful()) {
                    ServerTime serverTime = response.body();
                    Date serverDate = TimestampConverter.toTimestamp(serverTime.getCurrentTime());
                    Date nowDate = new Date();
                    diffTimeWithServer = (int) (serverDate.getTime() - nowDate.getTime()) / 1000;
                    hasDiffTime.set(true);
                }
            }

            @Override
            public void onFailure(IOException e) {
                Log.e(LOG_TAG, "Failed to get time from server. : " + e.getMessage());
                hasDiffTime.set(false);
            }
        });
    }

    private Reporter() {
    }
}
