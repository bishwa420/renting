package com.example.renting.appuser.controller;

import com.example.renting.annotation.NoTokenRequired;
import com.example.renting.appuser.model.SignupRequest;
import com.example.renting.appuser.service.UserService;
import com.example.renting.model.BasicRestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
            BasicRestResponse.message("User created successfully")
        );
    }
}
