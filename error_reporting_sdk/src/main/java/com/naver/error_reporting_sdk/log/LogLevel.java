package com.naver.error_reporting_sdk.log;

public enum LogLevel {
    VERBOSE("VERBOSE"),
    DEBUG("DEBUG"),
    INFO("INFO"),
    WARNING("WARNING"),
    ERROR("ERROR");

    private String name;

    LogLevel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
