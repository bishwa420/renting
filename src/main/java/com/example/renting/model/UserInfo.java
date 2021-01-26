package com.example.renting.model;

public class UserInfo {

    public Long userId;
    public String role;

    public static UserInfo of(Long userId, String role) {
        UserInfo userInfo = new UserInfo();
        userInfo.userId = userId;
        userInfo.role = role;
        return userInfo;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userId=" + userId +
                ", role='" + role + '\'' +
                '}';
    }
}
