package com.naver.error_reporting_sdk;

class ServerTime {
    private String currentTime;

    ServerTime(String currentTime) {
        this.currentTime = currentTime;
    }

    String getCurrentTime() {
        return currentTime;
    }

    @Override
    public String toString() {
        return "ServerTime{" +
                "currentTime='" + currentTime + '\'' +
                '}';
    }
}
