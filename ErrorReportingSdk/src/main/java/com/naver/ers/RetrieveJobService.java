package com.naver.ers;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.room.Room;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class RetrieveJobService extends JobService {
    private static final String LOG_TAG = RetrieveJobService.class.getSimpleName();

    private static final int INIT_DELAY = 0;
    private static final int PERIOD = 30;

    private ScheduledExecutorService executor;
    private WeakReference<Context> contextWeakReference;

    @Override
    public void onCreate() {
        super.onCreate();
        contextWeakReference = new WeakReference<Context>(this);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        if(executor != null && !executor.isShutdown()) {
            Log.d(LOG_TAG, "RetrieveJobService has already been running.");
            return true;
        }

        executor = Executors.newScheduledThreadPool(1);
        Log.d(LOG_TAG, "start to retrieve Error logs in DB.");

        executor.scheduleAtFixedRate(new DBTask(params), INIT_DELAY, PERIOD, TimeUnit.SECONDS);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        finish(params);
        return false;
    }

    /**
     * Runnable object for ScheduledExecutorService
     */
    private class DBTask implements Runnable {
        private JobParameters params;

        private DBTask(JobParameters params) {
            this.params = params;
        }
        /**
         * search and extract DB
         * if there is any log, execute {@link HttpSender} for sending it to server
         */
        @Override
        public void run() {
            Context context = (contextWeakReference != null) ? contextWeakReference.get() : null;
            if(context == null) {
                stopSelf();
                return;
            }

            if(!Reporter.hasDiffTime()) {
                Reporter.setDifferentTimeFromServer();
            }

            try {
                LogDatabase db = Room.databaseBuilder(context, LogDatabase.class, LogDatabase.DB_NAME).build();
                ErrorLogDao dao = db.errorLogDao();

                List<ErrorLog> errorLogs = dao.selectAllErrorLogs();
                for (ErrorLog log : errorLogs) {
                    Log.d(LOG_TAG, "retrieve and send the local data : " + log.toString());
                }
                dao.deleteLogs();
                db.close();

                if (errorLogs.isEmpty()) {
                    Log.d(LOG_TAG, "Retrieve Service shut down.");
                    finish(params);
                    return;
                }

                new HttpSender(context).send(errorLogs);
            } catch(Exception e) {
                Log.e(LOG_TAG, "occurred Exception while running DBTask : " + e.getMessage());
                finish(params);
            }
        }
    }

    private void finish(JobParameters params) {
        if(executor != null) {
            executor.shutdownNow();
        }
        jobFinished(params, false);
        stopSelf();
    }
}
