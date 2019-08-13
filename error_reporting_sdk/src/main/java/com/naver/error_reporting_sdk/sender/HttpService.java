package com.naver.error_reporting_sdk.sender;

import com.naver.error_reporting_sdk.db.ErrorLog;
import com.naver.error_reporting_sdk.db.ServerTime;
import com.naver.httpclientlib.CallTask;
import com.naver.httpclientlib.RequestMethod;
import com.naver.httpclientlib.annotation.RequestBody;
import com.naver.httpclientlib.annotation.RequestMapping;

import java.util.List;

public interface HttpService {
    @RequestMapping(value = "/ers/time", method=RequestMethod.GET)
    CallTask<ServerTime> getServerTime();

    @RequestMapping(value = "/ers/report", method=RequestMethod.POST)
    CallTask<ErrorLog> postLog(@RequestBody ErrorLog log);

    @RequestMapping(value = "/ers/reports", method=RequestMethod.POST)
    CallTask<ErrorLog> postLogs(@RequestBody List<ErrorLog> log);
}
