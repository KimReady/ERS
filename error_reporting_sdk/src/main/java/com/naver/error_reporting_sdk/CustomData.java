package com.naver.error_reporting_sdk;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class CustomData {
    private final String data;

    private CustomData(Builder builder) {
        this.data = builder.data;
    }

    public String getData() {
        return data;
    }

    public static final class Builder {
        private Map<String, String> dataMap;
        private String data;

        public Builder() {
            dataMap = new LinkedHashMap<>();
        }

        public Builder putData(String key, String value) {
            dataMap.put(key, value);
            return this;
        }

        public Builder putData(Map<String, String> map) {
            this.dataMap.putAll(map);
            return this;
        }

        public CustomData build() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("{\n");
            Iterator<String> iterator = dataMap.keySet().iterator();
            while(iterator.hasNext()) {
                String key = iterator.next();
                stringBuilder.append(key);
                stringBuilder.append(":");
                stringBuilder.append(dataMap.get(key));
                stringBuilder.append(",\n");
            }
            stringBuilder.append("}");
            data = stringBuilder.toString();
            return new CustomData(this);
        }
    }
}
