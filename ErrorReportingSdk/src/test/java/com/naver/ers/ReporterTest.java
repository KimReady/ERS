package com.naver.ers;


import android.app.Application;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ReporterTest {
    private Application application;

    @Before
    public void setUp() {
        application = ApplicationProvider.getApplicationContext();
        Reporter.register(application);
    }

    @Test
    public void registerContext() {
        assertNotNull(Reporter.getContext());
    }

    @Test
    public void reportError() {
        ReportInfo reportInfo = new ReportInfo.Builder(application)
                .logLevel(LogLevel.DEBUG.name())
                .tag("Test")
                .message("Test Message")
                .build();
        Reporter.reportError(reportInfo);
    }

    @Test
    public void setAndGetCustomData() {
        CustomData.Builder customDataBuilder = new CustomData.Builder();
        customDataBuilder.putData("name", "ready");
        customDataBuilder.putData("city", "seoul");
        customDataBuilder.putData("job", "programmer");
        CustomData customData = customDataBuilder.build();

        Reporter.setCustomData(customData);
        assertEquals(customData, Reporter.getCustomData());
    }

    @Test
    public void getAppVersion() {
        System.out.println(Reporter.getAppVersion());
    }

}