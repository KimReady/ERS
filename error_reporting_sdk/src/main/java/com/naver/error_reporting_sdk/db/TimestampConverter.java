package com.naver.error_reporting_sdk.db;

import androidx.room.TypeConverter;

import com.naver.error_reporting_sdk.Reporter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimestampConverter {
    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);

    @TypeConverter
    public static Date toTimestamp(String value) {
        if(value != null) {
            try {
                return dateFormat.parse(value);
            } catch(ParseException e) {
                Reporter.log.e(Reporter.LOG_TAG, e.getMessage());
            }
        }
        return null;
    }

    @TypeConverter
    public static String fromTimestamp(Date value) {
        return value != null ? dateFormat.format(value) : null;
    }

    private TimestampConverter() {}
}
