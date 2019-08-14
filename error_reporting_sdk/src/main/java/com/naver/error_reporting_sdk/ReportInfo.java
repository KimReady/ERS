package com.naver.error_reporting_sdk;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings.Secure;

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

    ReportInfo(Bundle bundle) {
        this.androidId = bundle.getString("android_id");
        this.packageName = bundle.getString("package_name");
        this.logLevel = bundle.getString("log_level");
        this.tag = bundle.getString("tag");
        this.message = bundle.getString("message");
        this.stackTrace = bundle.getString("stacktrace");
        this.availableMemory = bundle.getLong("available_memory");
        this.totalMemory = bundle.getLong("total_memory");
    }

    String getAndroidId() {
        return androidId;
    }

    int getSdkVersion() {
        return SDK_VERSION;
    }

    String getPhoneBrand() {
        return PHONE_BRAND;
    }

    String getPhoneModel() {
        return PHONE_MODEL;
    }

    String getPackageName() {
        return packageName;
    }

    String getStackTrace() {
        return stackTrace;
    }

    String getLogLevel() {
        return logLevel;
    }

    String getTag() {
        return tag;
    }

    String getMessage() {
        return message;
    }

    Context getContext() {
        return context;
    }

    long getAvailableMemory() {
        return availableMemory;
    }

    long getTotalMemory() {
        return totalMemory;
    }

    Bundle makeBundle() {
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

    static final class Builder {
        private final String androidId;
        private final String packageName;
        private String logLevel;
        private String tag;
        private String message;
        private Context context;
        private String stackTrace;

        Builder(Context context) {
            this.context = context;
            this.androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
            this.packageName = context.getPackageName();
            this.logLevel = LogLevel.ERROR.name();
            this.tag = "";
            this.message = "";
            this.stackTrace = "";
        }

        Builder logLevel(String logLevel) {
            this.logLevel = logLevel;
            return this;
        }

        Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        Builder message(String message) {
            this.message = message;
            return this;
        }

        Builder stackTrace(String stackTrace) {
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

        ReportInfo build() {
            return new ReportInfo(this);
        }
    }
}
