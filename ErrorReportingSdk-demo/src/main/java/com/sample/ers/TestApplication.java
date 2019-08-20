package com.sample.ers;

import android.app.Application;

import com.naver.ers.CustomData;
import com.naver.ers.Reporter;

public class TestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        CustomData.Builder customDataBuilder = new CustomData.Builder()
                .putData("name", "ReadyKim")
                .putData("Age", 26);
        Reporter.setCustomData(customDataBuilder.build());
        Reporter.register(this);
    }
}
