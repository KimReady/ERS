package com.naver.ers;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;
import java.util.Date;

/**
 * store the crash data or custom log
 */
@Entity(tableName = "error_log", primaryKeys = {"android_id", "reg_date"})
class ErrorLog {
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

    @ColumnInfo(name = "app_version")
    @SerializedName(value = "app_version")
    private String appVersion;

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

    @ColumnInfo(name = "tag")
    @SerializedName(value = "tag")
    private String tag;

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

    @ColumnInfo(name = "custom_data")
    @SerializedName(value = "custom_data")
    private String customData;

    @ColumnInfo(name = "is_correct_date")
    private boolean isCorrectDate;

    ErrorLog(@NonNull String regDate,
                    @NonNull String androidId,
                    String packageName,
                    String appVersion,
                    int sdkVersion,
                    String phoneBrand,
                    String phoneModel,
                    String logLevel,
                    String tag,
                    String message,
                    String stackTrace,
                    long availableMemory,
                    long totalMemory,
                    String customData,
                    boolean isCorrectDate) {
        this.regDate = regDate;
        this.androidId = androidId;
        this.packageName = packageName;
        this.appVersion = appVersion;
        this.sdkVersion = sdkVersion;
        this.phoneBrand = phoneBrand;
        this.phoneModel = phoneModel;
        this.logLevel = logLevel;
        this.tag = tag;
        this.message = message;
        this.stackTrace = stackTrace;
        this.availableMemory = availableMemory;
        this.totalMemory = totalMemory;
        this.customData = customData;
        this.isCorrectDate = isCorrectDate;
    }

    public ErrorLog(ReportInfo reportInfo) {
        this.regDate = Util.fromTimestamp(correctDate());
        this.androidId = reportInfo.getAndroidId();
        this.packageName = reportInfo.getPackageName();
        this.appVersion = Reporter.getAppVersion();
        this.sdkVersion = reportInfo.getSdkVersion();
        this.phoneBrand = reportInfo.getPhoneBrand();
        this.phoneModel = reportInfo.getPhoneModel();
        this.logLevel = reportInfo.getLogLevel();
        this.tag = reportInfo.getTag();
        this.message = reportInfo.getMessage();
        this.stackTrace = reportInfo.getStackTrace();
        this.availableMemory = reportInfo.getAvailableMemory();
        this.totalMemory = reportInfo.getTotalMemory();
        CustomData cd = Reporter.getCustomData();
        this.customData = cd != null ? cd.getData() : "";
    }

    String getAndroidId() {
        return androidId;
    }

    String getPackageName() {
        return packageName;
    }

    String getRegDate() {
        return regDate;
    }

    String getAppVersion() {
        return appVersion;
    }

    int getSdkVersion() {
        return sdkVersion;
    }

    String getPhoneBrand() {
        return phoneBrand;
    }

    String getPhoneModel() {
        return phoneModel;
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

    String getStackTrace() {
        return stackTrace;
    }

    long getAvailableMemory() {
        return availableMemory;
    }

    long getTotalMemory() {
        return totalMemory;
    }

    String getCustomData() {
        return customData;
    }

    boolean isCorrectDate() {
        return isCorrectDate;
    }

    /**
     * correct the value that the time calculation with the server has not been completed
     */
    void addDiffTimeWithServer() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(Util.toTimestamp(regDate));
        cal.add(Calendar.SECOND, Reporter.getDiffTimeWithServer());
        regDate = Util.fromTimestamp(cal.getTime());
        isCorrectDate = true;
    }

    /**
     * correct the time with server
     *
     * @return corrected time
     */
    private Date correctDate() {
        Calendar cal = Calendar.getInstance();
        if(Reporter.hasDiffTime()) {
            cal.add(Calendar.SECOND, Reporter.getDiffTimeWithServer());
            isCorrectDate = true;
        }
        return cal.getTime();
    }
}
