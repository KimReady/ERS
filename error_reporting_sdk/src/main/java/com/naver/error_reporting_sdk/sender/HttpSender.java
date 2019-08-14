package com.naver.error_reporting_sdk.sender;

import android.content.Context;
import android.util.Log;

import com.naver.error_reporting_sdk.R;
import com.naver.error_reporting_sdk.Reporter;
import com.naver.error_reporting_sdk.db.ErrorLog;
import com.naver.httpclientlib.CallTask;
import com.naver.httpclientlib.HttpClient;

import java.io.IOException;
import java.util.List;

public class HttpSender implements Sender {
    private static final String LOG_TAG = HttpSender.class.getSimpleName();
    private final Context context;
    private final HttpService httpService;

    public HttpSender(Context context) {
        this.context = context;
        HttpClient httpClient = new HttpClient.Builder()
                .baseUrl(context.getResources().getString(R.string.server_url))
                .build();
        httpService = httpClient.create(HttpService.class);
    }

    @Override
    public void send(ErrorLog errorLog) {
        try {
            if(!Reporter.hasDiffTime()) {
                throw new IllegalStateException();
            }

            if(!errorLog.isCorrectDate()) {
                errorLog.addDiffTimeWithServer();
            }

            CallTask<ErrorLog> callTask = httpService.postLog(errorLog);

            if (!callTask.execute().isSuccessful()) {
                throw new IllegalStateException();
            }

            Log.d(LOG_TAG, "Succeed to send the Error-Report.");
        } catch (IOException | IllegalStateException e) {
            Log.w(LOG_TAG, "failed to connect with server.");
            new DBSender(context).send(errorLog);
        }
    }

    @Override
    public void send(List<ErrorLog> errorLogs) {
        try {
            if (!Reporter.hasDiffTime()) {
                throw new IllegalStateException();
            }

            for (ErrorLog errorLog : errorLogs) {
                if (!errorLog.isCorrectDate()) {
                    errorLog.addDiffTimeWithServer();
                }
            }

            CallTask<ErrorLog> callTask = httpService.postLogs(errorLogs);
            if (!callTask.execute().isSuccessful()) {
                throw new IllegalStateException();
            }

            Log.d(LOG_TAG, "Succeed to send the Error-Report.");
        } catch (IOException | IllegalStateException e) {
            Log.w(LOG_TAG, "failed to connect with server.");
            new DBSender(context).send(errorLogs);
        }
    }
}
