package com.naver.ers;

import android.content.Context;
import android.util.Log;

import com.naver.httpclientlib.CallTask;
import com.naver.httpclientlib.HttpClient;

import java.io.IOException;
import java.util.List;

/**
 * send the log to server using {@link com.naver.httpclientlib}
 * if failed to send logs, execute {@link DBSender} for storing it to the local DB
 */
class HttpSender implements Sender {
    private static final String LOG_TAG = HttpSender.class.getSimpleName();
    private final Context context;
    private final HttpService httpService;

    HttpSender(Context context) {
        this.context = context;
        HttpClient httpClient = new HttpClient.Builder()
                .baseUrl(context.getResources().getString(R.string.server_url))
                .build();
        httpService = httpClient.create(HttpService.class);
    }

    /**
     * send a log to server
     */
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

    /**
     * send many of logs to server
     */
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
