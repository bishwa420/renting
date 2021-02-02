package com.example.renting.appuser.model.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class GoogleSignupRequest {

    @NotBlank(message = "Token is required")
    public String token;

    @NotBlank(message = "Role must be given")
    @Pattern(regexp = "REALTOR|CLIENT", message = "Role must either be 'REALTOR' or 'CLIENT'")
    public String role;
}
