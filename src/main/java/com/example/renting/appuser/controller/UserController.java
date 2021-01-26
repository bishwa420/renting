package com.example.renting.appuser.controller;

import com.example.renting.annotation.AdminPrivileged;
import com.example.renting.annotation.NoTokenRequired;
import com.example.renting.appuser.model.*;
import com.example.renting.appuser.service.UserService;
import com.example.renting.model.BasicRestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @NoTokenRequired
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BasicRestResponse> signup(@Valid @RequestBody SignupRequest request) {

        log.info("Received sign up request from user: {}", request);

        userService.signup(request);

        return ResponseEntity.ok(
            BasicRestResponse.message("User signed up successfully")
        );
    }

    @AdminPrivileged
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserListResponse> getUsersList(@RequestParam(name = "page", defaultValue = "1") int page,
                                                         @RequestParam(name = "limit", defaultValue = "10") int limit,
                                                         @RequestParam(name = "nameLike", required = false) String nameLike,
                                                         @RequestParam(name = "emailLike", required = false) String emailLike) {

        UserListResponse userListResponse = userService.getUserList(page, limit, nameLike, emailLike);
        return ResponseEntity.ok(userListResponse);
    }

    @AdminPrivileged
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BasicRestResponse> createUser(@Valid @RequestBody CreateUserRequest request) {

        userService.createUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BasicRestResponse.message("User created successfully"));
    }

    @AdminPrivileged
    @DeleteMapping(value = "")
    private ResponseEntity<BasicRestResponse> deleteUser(@Valid @RequestBody DeleteUserRequest request) {

        userService.suspendUser(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BasicRestResponse.message("User deleted successfully"));
    }

    @AdminPrivileged
    @PutMapping(value = "")
    public ResponseEntity<BasicRestResponse> updateUser(@Valid @RequestBody UpdateUserRequest request) {

        userService.updateUser(request);

        return ResponseEntity.ok(BasicRestResponse.message("User updated successfully"));
    }
}
