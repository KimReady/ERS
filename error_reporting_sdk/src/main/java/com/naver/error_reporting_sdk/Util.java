package com.naver.error_reporting_sdk;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Util {
    static final String LOG_TAG = Util.class.getSimpleName();

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

    public static String parseString(Throwable tr) {
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
