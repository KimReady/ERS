package com.naver.ers;

import java.util.List;

/**
 * Sender Interface
 */
interface Sender {
    void send(ErrorLog errorLog);

    void send(List<ErrorLog> errorLogs);
}
