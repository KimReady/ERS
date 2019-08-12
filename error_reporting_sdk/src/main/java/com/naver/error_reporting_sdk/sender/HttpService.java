package com.naver.error_reporting_sdk.sender;

import com.naver.error_reporting_sdk.db.ErrorLog;
import com.naver.error_reporting_sdk.db.ServerTime;
import com.naver.httpclientlib.CallTask;
import com.naver.httpclientlib.RequestMethod;
import com.naver.httpclientlib.annotation.RequestBody;
import com.naver.httpclientlib.annotation.RequestMapping;

public interface HttpService {
    @RequestMapping(value = "/ers/time", method=RequestMethod.GET)
    CallTask<ServerTime> getServerTimeWithDynamicURL();

    @RequestMapping(value = "/ers/report", method=RequestMethod.POST)
    CallTask<ErrorLog> postLog(@RequestBody ErrorLog log);
}
