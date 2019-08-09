package com.naver.error_reporting_sdk.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.naver.error_reporting_sdk.ReportInfo;
import com.naver.error_reporting_sdk.Reporter;

import java.util.Calendar;
import java.util.Date;

@Entity(tableName = "error_log", primaryKeys = {"ANDROID_ID", "REG_DATE"})
public final class ErrorLog {
    @NonNull
    @TypeConverters({TimestampConverter.class})
    @ColumnInfo(name = "REG_DATE")
    private Date regDate;

    @NonNull
    @ColumnInfo(name = "ANDROID_ID")
    private String androidId;

    @ColumnInfo(name = "PACKAGE_NAME")
    private String packageName;

    @ColumnInfo(name = "SDK_VERSION")
    private int sdkVersion;

    @ColumnInfo(name = "PHONE_BRAND")
    private String phoneBrand;

    @ColumnInfo(name = "PHONE_MODEL")
    private String phoneModel;

    @ColumnInfo(name = "LOG_LEVEL")
    private String logLevel;

    @ColumnInfo(name = "Message")
    private String message;

    @ColumnInfo(name = "StackTrace")
    private String stackTrace;

    @ColumnInfo(name = "Available_Memory")
    private long availableMemory;

    @ColumnInfo(name = "Total_Memory")
    private long totalMemory;

    public ErrorLog(@NonNull String androidId,
                    String packageName,
                    @NonNull Date regDate,
                    int sdkVersion,
                    String phoneBrand,
                    String phoneModel,
                    String logLevel,
                    String message,
                    String stackTrace,
                    long availableMemory,
                    long totalMemory) {
        this.androidId = androidId;
        this.packageName = packageName;
        this.regDate = regDate;
        this.sdkVersion = sdkVersion;
        this.phoneBrand = phoneBrand;
        this.phoneModel = phoneModel;
        this.logLevel = logLevel;
        this.message = message;
        this.stackTrace = stackTrace;
        this.availableMemory = availableMemory;
        this.totalMemory = totalMemory;
    }

    public ErrorLog(ReportInfo reportInfo) {
        this.androidId = reportInfo.getAndroidId();
        this.packageName = reportInfo.getPackageName();
        this.regDate = correctDate();
        this.sdkVersion = reportInfo.getSdkVersion();
        this.phoneBrand = reportInfo.getPhoneBrand();
        this.phoneModel = reportInfo.getPhoneModel();
        this.logLevel = reportInfo.getLogLevel();
        this.message = reportInfo.getMessage();
        this.stackTrace = reportInfo.getTopOfStackTrace();
        this.availableMemory = reportInfo.getAvailableMemory();
        this.totalMemory = reportInfo.getTotalMemory();
    }

    public String getAndroidId() {
        return androidId;
    }

    public String getPackageName() {
        return packageName;
    }

    public Date getRegDate() {
        return regDate;
    }

    public int getSdkVersion() {
        return sdkVersion;
    }

    public String getPhoneBrand() {
        return phoneBrand;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public String getMessage() {
        return message;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public long getAvailableMemory() {
        return availableMemory;
    }

    public long getTotalMemory() {
        return totalMemory;
    }

    private Date correctDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, Reporter.getDiffTimeWithServer());
        return cal.getTime();
    }

    @Override
    public String toString() {
        return "ErrorLog {" +
                "regDate=" + regDate +
                ", androidId='" + androidId + '\'' +
                ", packageName='" + packageName + '\'' +
                ", sdkVersion=" + sdkVersion +
                ", phoneBrand='" + phoneBrand + '\'' +
                ", phoneModel='" + phoneModel + '\'' +
                ", logLevel='" + logLevel + '\'' +
                ", message='" + message + '\'' +
                ", stackTrace='" + stackTrace + '\'' +
                ", availableMemory=" + availableMemory +
                ", totalMemory=" + totalMemory +
                '}';
    }
}
