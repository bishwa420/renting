package com.example.renting.appuser.model;


import javax.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank(message = "Email must be given")
    public String email;

    @NotBlank(message = "Password must be given")
    public String password;

    @Override
    public String toString() {
        return "LoginRequest{" +
                "email='" + email + '\'' +
                '}';
    }
}
