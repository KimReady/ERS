package com.naver.error_reporting_sdk;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.StatFs;
import android.provider.Settings.Secure;

import com.naver.error_reporting_sdk.log.LogLevel;

public final class ReportInfo implements Parcelable {
    private static final int SDK_VERSION = Build.VERSION.SDK_INT;
    private static final String PHONE_BRAND = Build.BRAND;
    private static final String PHONE_MODEL = Build.MODEL;
    private String androidId;
    private String packageName;
    private String logLevel;
    private String message;
    private String topOfStackTrace;
    private long availableMemory;
    private long totalMemory;

    private Context context;

    private ReportInfo(Builder builder) {
        this.androidId = builder.androidId;
        this.packageName = builder.packageName;
        this.logLevel = builder.logLevel;
        this.message = builder.message;
        this.topOfStackTrace = builder.topOfStackTrace;
        this.context = builder.context;
        this.availableMemory = builder.getAvailableInternalMemorySize();
        this.totalMemory = builder.getTotalInternalMemorySize();
    }

    private ReportInfo(Parcel parcel) {
        this.androidId = parcel.readString();
        this.packageName = parcel.readString();
        this.logLevel = parcel.readString();
        this.message = parcel.readString();
        this.topOfStackTrace = parcel.readString();
        this.availableMemory = parcel.readLong();
        this.totalMemory = parcel.readLong();
    }

    public static final Parcelable.Creator<ReportInfo> CREATOR = new Parcelable.Creator<ReportInfo>() {
        @Override
        public ReportInfo createFromParcel(Parcel parcel) {
            return new ReportInfo(parcel);
        }
        @Override
        public ReportInfo[] newArray(int size) {
            return new ReportInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(androidId);
        dest.writeString(packageName);
        dest.writeString(logLevel);
        dest.writeString(message);
        dest.writeString(topOfStackTrace);
        dest.writeLong(availableMemory);
        dest.writeLong(totalMemory);
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

    public String getLogLevel() {
        return logLevel;
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

    @Override
    public String toString() {
        return "ReportInfo{" +
                "sdkVersion=" + SDK_VERSION +
                ", phoneBrand='" + PHONE_BRAND + '\'' +
                ", phoneModel='" + PHONE_MODEL + '\'' +
                ", androidId='" + androidId + '\'' +
                ", packageName='" + packageName + '\'' +
                ", logLevel='" + logLevel + '\'' +
                ", message='" + message + '\'' +
                ", topOfStackTrace='" + topOfStackTrace + '\'' +
                ", context=" + context +
                ", availableMemory=" + availableMemory +
                ", totalMemory=" + totalMemory +
                '}';
    }

    public static final class Builder {
        private final String androidId;
        private final String packageName;
        private String logLevel;
        private String message;
        private Context context;
        private String topOfStackTrace;

        public Builder(Context context) {
            this.context = context;
            this.androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
            this.packageName = context.getPackageName();
            this.logLevel = LogLevel.ERROR.getName();
        }

        public Builder logLevel(LogLevel logLevel) {
            this.logLevel = logLevel.getName();
            return this;
        }

        public Builder message(String message) {
            this.message = message;
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
