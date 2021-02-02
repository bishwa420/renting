package com.example.renting.appuser.model.request;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CreateUserRequest {

    @NotNull(message = "Name must be given")
    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 1, max = 100, message = "Name must be between 1 to 100 characters")
    public String name;

    @NotNull(message = "Password must be given")
    @NotEmpty(message = "Password cannot be empty")
    @Size(max = 50, message = "Password must be between 1 to 50 characters")
    public String password;

    @NotNull(message = "Email must be given")
    @NotEmpty(message = "Email cannot be empty")
    @Size(max = 100, message = "Email must be between 1 to 100 characters")
    @Pattern(regexp = "^[A-Za-z0-9][A-Za-z0-9._-]*@[A-Za-z0-9][A-Za-z0-9._-]*\\.([A-Za-z0-9][A-Za-z0-9_-]\\.)*[A-Za-z]{2,}$"
            , message = "Email address must be valid")
    public String email;

    @NotNull(message = "Role must be given")
    @NotEmpty(message = "Role cannot be empty")
    @Pattern(regexp = "ADMIN|REALTOR|CLIENT", message = "Role must either be 'ADMIN', or 'REALTOR', or 'CLIENT'")
    public String role;

    @Override
    public String toString() {
        return "CreateUserRequest{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
