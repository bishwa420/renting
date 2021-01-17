package com.example.renting.appuser.model;

import com.example.renting.model.BasicRestResponse;

public class LoginResponse extends BasicRestResponse {

    public String token;

    public static LoginResponse of(String token) {

        LoginResponse response = new LoginResponse();
        response.message = "Login success";
        response.token = token;
        return response;
    }
}
