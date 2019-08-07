package com.naver.error_reporting_sdk;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings.Secure;

import com.naver.error_reporting_sdk.log.CustomLog;
import com.naver.error_reporting_sdk.sender.Sender;

import java.util.ArrayList;
import java.util.List;

public class ReportInfo {
    private static final int SDK_VERSION = Build.VERSION.SDK_INT;
    private static final String PHONE_BRAND = Build.BRAND;
    private static final String PHONE_MODEL = Build.MODEL;

    private final String androidId;
    private final String packageName;
    private final String message;
    private final String topOfStackTrace;
    private final List<CustomLog> customLogs;
    private final Context context;
    private final long availableMemory;
    private final long totalMemory;

    ReportInfo(Builder builder) {
        this.androidId = builder.androidId;
        this.packageName = builder.packageName;
        this.message = builder.message;
        this.topOfStackTrace = builder.topOfStackTrace;
        this.customLogs = builder.customLogs;
        this.context = builder.context;
        this.availableMemory = builder.getAvailableInternalMemorySize();
        this.totalMemory = builder.getTotalInternalMemorySize();
    }

    public String getAndroidId() {
        return androidId;
    }

    public int getSdkVersion() {
        return SDK_VERSION;
    }

    public String getPhoneBrand() {
        return PHONE_BRAND;
    }

    public String getPhoneModel() {
        return PHONE_MODEL;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getTopOfStackTrace() {
        return topOfStackTrace;
    }

    public String getMessage() {
        return message;
    }

    public List<CustomLog> getCustomLogs() {
        return customLogs;
    }

    public Context getContext() {
        return context;
    }

    public long getAvailableMemory() {
        return availableMemory;
    }

    public long getTotalMemory() {
        return totalMemory;
    }

    public void execute(Sender sender) {
        sender.send(this);
    }

    static class Builder {
        private final String androidId;
        private final String packageName;
        private String message;
        private List<CustomLog> customLogs;
        private Context context;
        private String topOfStackTrace;

        public Builder(Context context) {
            this.context = context;
            this.customLogs = new ArrayList<>();
            androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
            packageName = context.getPackageName();
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder putCustomLog(String key, String value) {
            customLogs.add(new CustomLog(key, value));
            return this;
        }

        public Builder topOfStackTrace(String topOfStackTrace) {
            this.topOfStackTrace = topOfStackTrace;
            return this;
        }

        private long getAvailableInternalMemorySize() {
            StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
            return stat.getAvailableBlocks() * stat.getBlockSize();
        }

        private long getTotalInternalMemorySize() {
            StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
            return stat.getBlockCount() * stat.getBlockSize();
        }

        public ReportInfo build() {
            return new ReportInfo(this);
        }
    }
}
