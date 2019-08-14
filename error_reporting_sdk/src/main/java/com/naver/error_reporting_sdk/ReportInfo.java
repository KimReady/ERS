package com.naver.error_reporting_sdk;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings.Secure;

import com.naver.error_reporting_sdk.log.LogLevel;

public final class ReportInfo {
    private static final int SDK_VERSION = Build.VERSION.SDK_INT;
    private static final String PHONE_BRAND = Build.BRAND;
    private static final String PHONE_MODEL = Build.MODEL;
    private String androidId;
    private String packageName;
    private String logLevel;
    private String tag;
    private String message;
    private String stackTrace;
    private long availableMemory;
    private long totalMemory;

    private Context context;

    private ReportInfo(Builder builder) {
        this.androidId = builder.androidId;
        this.packageName = builder.packageName;
        this.logLevel = builder.logLevel;
        this.tag = builder.tag;
        this.message = builder.message;
        this.stackTrace = builder.stackTrace;
        this.context = builder.context;
        this.availableMemory = builder.getAvailableInternalMemorySize();
        this.totalMemory = builder.getTotalInternalMemorySize();
    }

    public ReportInfo(Bundle bundle) {
        this.androidId = bundle.getString("android_id");
        this.packageName = bundle.getString("package_name");
        this.logLevel = bundle.getString("log_level");
        this.tag = bundle.getString("tag");
        this.message = bundle.getString("message");
        this.stackTrace = bundle.getString("stacktrace");
        this.availableMemory = bundle.getLong("available_memory");
        this.totalMemory = bundle.getLong("total_memory");
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

    public String getStackTrace() {
        return stackTrace;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public String getTag() {
        return tag;
    }

    public String getMessage() {
        return message;
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

    public Bundle makeBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("android_id", androidId);
        bundle.putString("package_name", packageName);
        bundle.putString("log_level", logLevel);
        bundle.putString("tag", tag);
        bundle.putString("message", message);
        bundle.putString("stacktrace", stackTrace);
        bundle.putLong("available_memory", availableMemory);
        bundle.putLong("total_memory", totalMemory);
        return bundle;
    }

    @Override
    public String toString() {
        return "ReportInfo{" +
                "sdkVersion=" + SDK_VERSION +
                ", phoneBrand='" + PHONE_BRAND + '\'' +
                ", phoneModel='" + PHONE_MODEL + '\'' +
                ", androidId='" + androidId + '\'' +
                ", packageName='" + packageName + '\'' +
                ", logLevel='" + logLevel + '\'' +
                ", tag='" + tag + '\'' +
                ", message='" + message + '\'' +
                ", StackTrace='" + stackTrace + '\'' +
                ", context=" + context +
                ", availableMemory=" + availableMemory +
                ", totalMemory=" + totalMemory +
                '}';
    }

    public static final class Builder {
        private final String androidId;
        private final String packageName;
        private String logLevel;
        private String tag;
        private String message;
        private Context context;
        private String stackTrace;

        public Builder(Context context) {
            this.context = context;
            this.androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
            this.packageName = context.getPackageName();
            this.logLevel = LogLevel.ERROR.name();
            this.tag = "";
            this.message = "";
            this.stackTrace = "";
        }

        public Builder logLevel(String logLevel) {
            this.logLevel = logLevel;
            return this;
        }

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder stackTrace(String stackTrace) {
            this.stackTrace = stackTrace;
            return this;
        }

        private long getAvailableInternalMemorySize() {
            StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
            if(SDK_VERSION >= 18) {
                return stat.getAvailableBlocksLong() * stat.getBlockSizeLong();
            } else {
                return stat.getAvailableBlocks() * stat.getBlockSize();
            }
        }

        private long getTotalInternalMemorySize() {
            StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
            if(SDK_VERSION >= 18) {
                return stat.getBlockCountLong() * stat.getBlockSizeLong();
            } else {
                return stat.getBlockCount() * stat.getBlockSize();
            }
        }

        public ReportInfo build() {
            return new ReportInfo(this);
        }
    }
}
