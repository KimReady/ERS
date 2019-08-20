package com.naver.ers;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class RetrieveJobScheduler {
    private static final int JOB_ID = 0x1000;

    static void setUpdateJob(Context context) {
        if(isJobServiceOn(context)) {
            return;
        }

        JobInfo job = new JobInfo.Builder(JOB_ID, new ComponentName(context, RetrieveJobService.class))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build();
        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        scheduler.schedule(job);
    }

    static boolean isJobServiceOn(Context context) {
        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        boolean hasBeenScheduled = false;

        for(JobInfo jobInfo : scheduler.getAllPendingJobs()) {
            if(jobInfo.getId() == JOB_ID) {
                hasBeenScheduled = true;
            }
        }
        return hasBeenScheduled;
    }

    private RetrieveJobScheduler(){}
}
