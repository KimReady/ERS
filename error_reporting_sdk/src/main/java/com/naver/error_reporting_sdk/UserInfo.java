package com.naver.error_reporting_sdk;

public class UserInfo {
    private final String company;
    private final String userName;
    private final String email;

    private UserInfo(Builder builder) {
        this.company = checkNotNull(builder.company);
        this.userName = checkNotNull(builder.userName);
        this.email = checkNotNull(builder.email);
    }

    public String getCompany() {
        return company;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    private String checkNotNull(String str) {
        return str != null ? str : "";
    }

    public static final class Builder {
        private String company;
        private String userName;
        private String email;

        public Builder company(String company) {
            this.company = company;
            return this;
        }

        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public UserInfo build() {
            return new UserInfo(this);
        }
    }
}
