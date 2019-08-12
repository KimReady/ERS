package com.naver.error_reporting_sdk.sender;

import com.naver.error_reporting_sdk.db.ErrorLog;
import com.naver.error_reporting_sdk.db.ServerTime;
import com.naver.httpclientlib.CallTask;
import com.naver.httpclientlib.RequestMethod;
import com.naver.httpclientlib.annotation.DynamicURL;
import com.naver.httpclientlib.annotation.RequestBody;
import com.naver.httpclientlib.annotation.URL;

public interface HttpService {
    @DynamicURL(method=RequestMethod.GET)
    CallTask<ServerTime> getServerTimeWithDynamicURL(@URL String url);

    @DynamicURL(method=RequestMethod.POST)
    CallTask<ErrorLog> postLog(@URL String url, @RequestBody ErrorLog log);
}
