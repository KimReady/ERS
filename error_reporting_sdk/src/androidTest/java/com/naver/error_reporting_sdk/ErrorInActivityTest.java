package com.naver.error_reporting_sdk;

import android.widget.TextView;

import org.junit.Test;

public class ErrorInActivityTest {

    @Test
    public void causeNullPointerExceptionWhenToClickView() {
        TextView title = null;
        title.performClick();
    }
}
