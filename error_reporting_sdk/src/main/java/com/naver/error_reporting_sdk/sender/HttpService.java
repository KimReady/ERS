package com.naver.error_reporting_sdk.sender;

import com.naver.error_reporting_sdk.db.ServerTime;
import com.naver.httpclientlib.CallTask;
import com.naver.httpclientlib.RequestMethod;
import com.naver.httpclientlib.annotation.DynamicURL;
import com.naver.httpclientlib.annotation.RequestMapping;
import com.naver.httpclientlib.annotation.URL;

public interface HttpService {
    @RequestMapping(value="/time", method= RequestMethod.GET)
    CallTask<ServerTime> getServerTime();

    @DynamicURL(method= RequestMethod.GET)
    CallTask<ServerTime> getServerTimeWithDynamicURL(@URL String url);

}
