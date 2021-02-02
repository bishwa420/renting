package com.example.renting.appuser.model.response;

import com.example.renting.appuser.db.entity.User;
import com.example.renting.model.BasicRestResponse;

public class LoginResponse extends BasicRestResponse {

    public String token;
    public String role;

    public static LoginResponse of(String token, User user) {

        LoginResponse response = new LoginResponse();
        response.message = "Login success";
        response.token = token;
        response.role = user.getRole().get();
        return response;
    }
}
