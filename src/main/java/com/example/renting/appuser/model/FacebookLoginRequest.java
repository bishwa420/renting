package com.example.renting.appuser.model;

import javax.validation.constraints.NotBlank;

public class FacebookLoginRequest {

    @NotBlank(message = "Token is required")
    public String token;
}
