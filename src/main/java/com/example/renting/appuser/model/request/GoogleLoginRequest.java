package com.example.renting.appuser.model.request;

import javax.validation.constraints.NotBlank;

public class GoogleLoginRequest {

    @NotBlank(message = "Token is required")
    public String token;
}
