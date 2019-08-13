package com.naver.error_reporting_sdk.sender;

import com.naver.error_reporting_sdk.db.ErrorLog;

import java.util.List;

public interface Sender {
    void send(ErrorLog errorLog);

    void send(List<ErrorLog> errorLogs);
}
