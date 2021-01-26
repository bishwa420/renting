package com.example.renting.appuser.model;

import javax.validation.constraints.*;

public class SignupRequest extends CreateUserRequest {

    @NotNull(message = "Password must be given")
    @NotEmpty(message = "Password cannot be empty")
    @Size(max = 50, message = "Password must be between 1 to 50 characters")
    public String password;

    @Override
    public String toString() {
        return "SignupRequest{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
