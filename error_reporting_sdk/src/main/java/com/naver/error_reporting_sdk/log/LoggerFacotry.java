package com.naver.error_reporting_sdk.log;

import android.util.Log;

import com.naver.error_reporting_sdk.ReportInfo;

public class LoggerFacotry {

    public static Logger create() {
        return new LoggerImpl(new ReportInfo());
    }

    public static Logger createStub() {
        return new LoggerStub();
    }

}
