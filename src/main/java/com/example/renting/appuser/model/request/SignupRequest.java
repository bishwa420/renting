package com.example.renting.appuser.model.request;

public class SignupRequest extends CreateUserRequest {

    @Override
    public String toString() {
        return "SignupRequest{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
