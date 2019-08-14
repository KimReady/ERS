package com.naver.error_reporting_sdk;

import com.naver.httpclientlib.CallTask;
import com.naver.httpclientlib.RequestMethod;
import com.naver.httpclientlib.annotation.RequestBody;
import com.naver.httpclientlib.annotation.RequestMapping;

import java.util.List;

interface HttpService {
    @RequestMapping(value = "/ers/time", method=RequestMethod.GET)
    CallTask<ServerTime> getServerTime();

    @RequestMapping(value = "/ers/report", method=RequestMethod.POST)
    CallTask<ErrorLog> postLog(@RequestBody ErrorLog log);

    @RequestMapping(value = "/ers/reports", method=RequestMethod.POST)
    CallTask<ErrorLog> postLogs(@RequestBody List<ErrorLog> log);
}
