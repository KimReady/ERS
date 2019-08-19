package com.naver.ers;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * provide utility methods statically
 * it can't be instantiated
 */
class Util {
    private static final String LOG_TAG = Util.class.getSimpleName();

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * convert a time value from a string type to a date type
     *
     * @param value time value with string type
     * @return time value with date type
     */
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

    /**
     * convert a time value from a date type to a string type
     *
     * @param value time value with date type
     * @return time value with string type
     */
    static String fromTimestamp(Date value) {
        return value != null ?
                new SimpleDateFormat(DATE_FORMAT, Locale.KOREA).format(value)
                : null;
    }

    /**
     * parse the stack trace in Throwable to String type
     *
     * @param tr Throwable object that has a stack trace
     * @return converted stack trace
     */
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
