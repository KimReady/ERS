package com.naver.error_reporting_sdk.log;

import android.content.Context;

public class LoggerFactory {
    private LoggerFactory(){}

    public static Logger create(Context context) {
        return new LoggerImpl(context);
    }

    public static Logger createStub() {
        return new LoggerStub();
    }

}
