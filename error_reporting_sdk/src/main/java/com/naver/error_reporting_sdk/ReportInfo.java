package com.naver.error_reporting_sdk;

import java.util.ArrayList;
import java.util.List;

public class ReportInfo {
    private List<Bundle> data;

    public ReportInfo() {
        data = new ArrayList<>();
    }

    public void put(String name, String value) {
        data.add(new Bundle(name, value));
    }

    private static class Bundle {
        private String key;
        private String value;

        Bundle(String key, String value) {
            this.key = key;
            this.value = value;
        }

        String getKey() {
            return key;
        }

        String getValue() {
            return value;
        }
    }
}
