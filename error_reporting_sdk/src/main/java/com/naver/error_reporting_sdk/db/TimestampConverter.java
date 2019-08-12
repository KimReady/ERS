package com.naver.error_reporting_sdk.db;

import com.naver.error_reporting_sdk.Reporter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimestampConverter {
    static final String LOG_TAG = TimestampConverter.class.getSimpleName();

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);

    public static Date toTimestamp(String value) {
        if(value != null) {
            try {
                return dateFormat.parse(value);
            } catch(ParseException e) {
                Reporter.log.e(LOG_TAG, e.getMessage());
            }
        }
        return null;
    }

    public static String fromTimestamp(Date value) {
        return value != null ? dateFormat.format(value) : null;
    }

    private TimestampConverter() {}
}
