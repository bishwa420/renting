package com.example.renting.appuser.model;

import javax.validation.constraints.*;

public class UpdateUserRequest {

    @NotNull(message = "User ID must be given")
    public Long id;

    @NotBlank(message = "Email must be given")
    @Size(max = 100, message = "Email must be between 1 to 100 characters")
    @Pattern(regexp = "^[A-Za-z0-9][A-Za-z0-9._-]*@[A-Za-z0-9][A-Za-z0-9._-]*\\.([A-Za-z0-9][A-Za-z0-9_-]\\.)*[A-Za-z]{2,}$"
            , message = "Email address must be valid")
    public String email;

    @NotBlank(message = "Name must be given")
    @Size(min = 1, max = 100, message = "Name must be between 1 to 100 characters")
    public String name;

    @NotBlank(message = "Role cannot be empty")
    @Pattern(regexp = "ADMIN|REALTOR|CLIENT", message = "Role must either be 'ADMIN', or 'REALTOR', or 'CLIENT'")
    public String role;

    @NotNull(message = "Suspend instruction cannot be empty")
    public Boolean doSuspend;

    @Override
    public String toString() {
        return "UpdateUserRequest{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
