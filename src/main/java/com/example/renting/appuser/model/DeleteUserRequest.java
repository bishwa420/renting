package com.example.renting.appuser.model;

import javax.validation.constraints.NotBlank;

public class DeleteUserRequest {

    @NotBlank(message = "Email must be given")
    public String email;
}
