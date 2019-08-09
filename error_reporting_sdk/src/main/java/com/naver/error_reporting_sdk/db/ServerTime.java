package com.naver.error_reporting_sdk.db;

public class ServerTime {
    private String currentTime;

    public ServerTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    @Override
    public String toString() {
        return "ServerTime{" +
                "currentTime='" + currentTime + '\'' +
                '}';
    }
}
