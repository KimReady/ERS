package com.naver.error_reporting_sdk;

import java.util.List;

interface Sender {
    void send(ErrorLog errorLog);

    void send(List<ErrorLog> errorLogs);
}
