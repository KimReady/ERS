package com.naver.ers;

import android.app.Application;

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
