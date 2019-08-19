package com.naver.ers;

import com.naver.httpclientlib.CallTask;
import com.naver.httpclientlib.RequestMethod;
import com.naver.httpclientlib.annotation.RequestBody;
import com.naver.httpclientlib.annotation.RequestMapping;

import java.util.List;

/**
 * needed for {@link com.naver.httpclientlib.HttpClient}
 * each methods will send a request to the relative url
 */
interface HttpService {
    /**
     * get time from server for calculating difference of device time with server
     *
     * @return object of {@link ServerTime}
     */
    @RequestMapping(value = "/ers/time", method=RequestMethod.GET)
    CallTask<ServerTime> getServerTime();

    /**
     * send a error log to server
     *
     * @param log object of {@link ErrorLog}
     */
    @RequestMapping(value = "/ers/report", method=RequestMethod.POST)
    CallTask<ErrorLog> postLog(@RequestBody ErrorLog log);

    /**
     * send the error logs to server
     *
     * @param log object of {@link ErrorLog}
     */
    @RequestMapping(value = "/ers/reports", method=RequestMethod.POST)
    CallTask<ErrorLog> postLogs(@RequestBody List<ErrorLog> log);
}
