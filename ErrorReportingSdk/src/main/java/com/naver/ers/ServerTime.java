package com.naver.ers;

/**
 * has time of server
 */
class ServerTime {
    private String currentTime;

    ServerTime(String currentTime) {
        this.currentTime = currentTime;
    }

    String getCurrentTime() {
        return currentTime;
    }
}
