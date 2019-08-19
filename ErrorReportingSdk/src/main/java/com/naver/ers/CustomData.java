package com.naver.ers;

import android.os.Bundle;

import java.util.Iterator;

/**
 * store the custom data
 * Nothing except Builder class is revealed
 */
public class CustomData {
    private final String data;

    private CustomData(Builder builder) {
        this.data = builder.data;
    }

    String getData() {
        return data;
    }

    /**
     * for building custom data, use this Builder class
     * the custom data consists of bundle
     * but when executing build(), transform to String
     */
    public static final class Builder {
        private Bundle bundle;
        private String data;

        public Builder() {
            this.bundle = new Bundle();
        }

        public Builder putData(Bundle bundle) {
            this.bundle.putAll(bundle);
            return this;
        }

        public Builder putData(String key, String value) {
            bundle.putString(key, value);
            return this;
        }

        public Builder putData(String key, int value) {
            bundle.putInt(key, value);
            return this;
        }

        public Builder putData(String key, float value) {
            bundle.putFloat(key, value);
            return this;
        }

        public CustomData build() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("{\n");
            Iterator<String> iterator = bundle.keySet().iterator();
            while(iterator.hasNext()) {
                String key = iterator.next();
                stringBuilder.append(key);
                stringBuilder.append(":");
                stringBuilder.append(bundle.get(key));
                stringBuilder.append(",\n");
            }
            stringBuilder.append("}");
            data = stringBuilder.toString();
            return new CustomData(this);
        }
    }
}
