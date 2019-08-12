package com.naver.error_reporting_sdk.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;
import com.naver.error_reporting_sdk.ReportInfo;
import com.naver.error_reporting_sdk.Reporter;

import java.util.Calendar;
import java.util.Date;

@Entity(tableName = "error_log", primaryKeys = {"android_id", "reg_date"})
public final class ErrorLog {
    @NonNull
    @ColumnInfo(name = "reg_date")
    @SerializedName(value = "reg_date")
    private String regDate;

    @NonNull
    @ColumnInfo(name = "android_id")
    @SerializedName(value = "android_id")
    private String androidId;

    @ColumnInfo(name = "package_name")
    @SerializedName(value = "package_name")
    private String packageName;

    @ColumnInfo(name = "sdk_version")
    @SerializedName(value = "sdk_version")
    private int sdkVersion;

    @ColumnInfo(name = "phone_brand")
    @SerializedName(value = "phone_brand")
    private String phoneBrand;

    @ColumnInfo(name = "phone_model")
    @SerializedName(value = "phone_model")
    private String phoneModel;

    @ColumnInfo(name = "log_level")
    @SerializedName(value = "log_level")
    private String logLevel;

    @ColumnInfo(name = "message")
    @SerializedName(value = "message")
    private String message;

    @ColumnInfo(name = "stacktrace")
    @SerializedName(value = "stacktrace")
    private String stackTrace;

    @ColumnInfo(name = "available_memory")
    @SerializedName(value = "available_memory")
    private long availableMemory;

    @ColumnInfo(name = "total_memory")
    @SerializedName(value = "total_memory")
    private long totalMemory;

    @ColumnInfo(name = "company")
    @SerializedName(value = "company")
    private String company;

    @ColumnInfo(name = "name")
    @SerializedName(value = "name")
    private String name;

    @ColumnInfo(name = "email")
    @SerializedName(value = "email")
    private String email;

    @ColumnInfo(name = "is_correct_date")
    private boolean isCorrectDate;

    public ErrorLog(@NonNull String androidId,
                    String packageName,
                    @NonNull String regDate,
                    int sdkVersion,
                    String phoneBrand,
                    String phoneModel,
                    String logLevel,
                    String message,
                    String stackTrace,
                    long availableMemory,
                    long totalMemory,
                    String company,
                    String name,
                    String email,
                    boolean isCorrectDate) {
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
        this.company = company;
        this.name = name;
        this.email = email;
        this.isCorrectDate = isCorrectDate;
    }

    public ErrorLog(ReportInfo reportInfo) {
        this.androidId = reportInfo.getAndroidId();
        this.packageName = reportInfo.getPackageName();
        this.regDate = TimestampConverter.fromTimestamp(correctDate());
        this.sdkVersion = reportInfo.getSdkVersion();
        this.phoneBrand = reportInfo.getPhoneBrand();
        this.phoneModel = reportInfo.getPhoneModel();
        this.logLevel = reportInfo.getLogLevel();
        this.message = reportInfo.getMessage();
        this.stackTrace = reportInfo.getStackTrace();
        this.availableMemory = reportInfo.getAvailableMemory();
        this.totalMemory = reportInfo.getTotalMemory();
        this.company = Reporter.getCompany();
        this.name = Reporter.getName();
        this.email = Reporter.getEmail();
        this.isCorrectDate = Reporter.hasDiffTime();
    }

    public String getAndroidId() {
        return androidId;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getRegDate() {
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

    public String getCompany() {
        return company;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean isCorrectDate() {
        return isCorrectDate;
    }

    public void addDiffTimeWithServer() {
        if(Reporter.hasDiffTime()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(TimestampConverter.toTimestamp(regDate));
            cal.add(Calendar.SECOND, Reporter.getDiffTimeWithServer());
            isCorrectDate = true;
        }
    }

    private Date correctDate() {
        Calendar cal = Calendar.getInstance();
        if(Reporter.hasDiffTime()) {
            cal.add(Calendar.SECOND, Reporter.getDiffTimeWithServer());
        }
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
                ", company=" + company +
                ", name=" + name +
                ", email=" + email +
                ", isCorrectDate=" + isCorrectDate +
                '}';
    }
}
