package com.naver.error_reporting_sdk;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class Util {
    private static final String LOG_TAG = Util.class.getSimpleName();

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    static Date toTimestamp(String value) {
        if(value != null) {
            try {
                DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.KOREA);
                return dateFormat.parse(value);
            } catch(ParseException e) {
                Reporter.log.e(LOG_TAG, e.getMessage());
            }
        }
        return null;
    }

    static String fromTimestamp(Date value) {
        return value != null ?
                new SimpleDateFormat(DATE_FORMAT, Locale.KOREA).format(value)
                : null;
    }

    static String parseString(Throwable tr) {
        if(tr == null) {
            return "";
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        String stackTrace = sw.toString();
        try {
            sw.close();
            pw.close();
        } catch(IOException e) {
            Reporter.log.w(LOG_TAG, "occurred Exception while closing StringWriter.");
        }
        return stackTrace;
    }

    private Util() {}
}
