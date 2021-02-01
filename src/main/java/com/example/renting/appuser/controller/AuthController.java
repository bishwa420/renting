package com.example.renting.appuser.controller;

import com.example.renting.annotation.NoTokenRequired;
import com.example.renting.appuser.model.FacebookLoginRequest;
import com.example.renting.appuser.model.GoogleLoginRequest;
import com.example.renting.appuser.model.LoginRequest;
import com.example.renting.appuser.model.LoginResponse;
import com.example.renting.appuser.service.AuthService;
import com.example.renting.model.BasicRestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(value = "*")
@RestController
@RequestMapping("/")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @NoTokenRequired
    @PostMapping(value = "login", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity login(@RequestBody @Valid LoginRequest request) {

        LoginResponse response = authService.getToken(request);

        return ResponseEntity.ok(response);
    }

    @NoTokenRequired
    @PostMapping(value = "login/google", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity loginWithGoogle(@RequestBody @Valid GoogleLoginRequest request) {

        LoginResponse response = authService.getTokenForGoogleLogin(request);
        return ResponseEntity.ok(response);
    }

    @NoTokenRequired
    @PostMapping(value = "login/facebook", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity loginWithFacebook(@Valid @RequestBody FacebookLoginRequest request) {

        LoginResponse response = authService.getTokenForFacebookLogin(request);
        return ResponseEntity.ok(response);
    }

    @NoTokenRequired
    @GetMapping(value = "/verification")
    public ResponseEntity verify(@RequestHeader(name = "verificationParam") String verificationParam) {

        authService.verifyUser(verificationParam);

        return ResponseEntity.ok(BasicRestResponse.message("User has been verified successfully"));
    }
}
