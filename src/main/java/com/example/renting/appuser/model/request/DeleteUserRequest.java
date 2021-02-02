package com.example.renting.appuser.model.request;

import javax.validation.constraints.NotBlank;

public class DeleteUserRequest {

    @NotBlank(message = "Email must be given")
    public String email;
}
