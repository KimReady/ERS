package com.naver.error_reporting_sdk;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class UtilTest {

    @Test
    public void toTimestamp() {
        String stringDate = "1994-05-17 09:02:05";
        Date date = Util.toTimestamp(stringDate);
        System.out.println(date.toString());
    }

    @Test(expected = RuntimeException.class)
    public void failToTimestamp() {
        String stringDate = "1994-0517 09:02:05";
        Util.toTimestamp(stringDate);
    }

    @Test
    public void fromTimestamp() {
        Date now = new Date();
        String stringNow = Util.fromTimestamp(now);
        System.out.println(stringNow);
    }

    @Test
    public void fromAndToTimestamp() {
        String stringDate = "1994-05-17 09:02:05";
        Date date = Util.toTimestamp(stringDate);
        String reDate = Util.fromTimestamp(date);
        assertEquals(stringDate, reDate);
    }

    @Test
    public void parseStringThrowable() {
        String stacktrace = Util.parseString(new Throwable("Test Throwable"));
        System.out.println(stacktrace);
    }

    @Test
    public void parseStringException() {
        String stacktrace = Util.parseString(new RuntimeException("Test Throwable"));
        System.out.println(stacktrace);
    }
}