package com.example.renting.appuser.model.request;

import javax.validation.constraints.NotBlank;

public class FacebookSignupRequest {

    @NotBlank(message = "Token is required")
    public String token;

    @NotBlank(message = "User role is required")
    public String role;
}
