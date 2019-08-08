package com.naver.error_reporting_sdk.sender;

import com.naver.error_reporting_sdk.ReportInfo;

public interface Sender {

    void send(ReportInfo reportInfo);
}
