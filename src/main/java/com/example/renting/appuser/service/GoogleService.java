package com.example.renting.appuser.service;

import com.example.renting.appuser.db.entity.User;
import com.example.renting.appuser.model.request.GoogleLoginRequest;
import com.example.renting.appuser.model.request.GoogleSignupRequest;
import com.example.renting.appuser.model.response.LoginResponse;
import com.example.renting.appuser.model.thirdparty.GoogleUser;
import com.example.renting.exception.UnauthorizedException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class GoogleService {

    private static final Logger log = LoggerFactory.getLogger(GoogleService.class);

    private static final String GOOGLE_CLIENT_ID = "758898908443-kvlhgb8bpbtfs0jam1kq6i9m4bc1vst5.apps.googleusercontent.com";


    private GoogleUser getGoogleUser(String token) {

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                .build();

        try {
            GoogleIdToken googleIdToken = verifier.verify(token);
            if(googleIdToken == null) {
                throw UnauthorizedException.ex("Google authentication failed");
            }

            GoogleIdToken.Payload payload = googleIdToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");

            return GoogleUser.of(name, email);

        } catch (GeneralSecurityException | IOException e) {
            log.error("Google ID token verification exception: {}", e.getMessage(), e);
            throw UnauthorizedException.ex("Invalid Google authentication");
        }
    }

    public GoogleUser getGoogleUser(GoogleLoginRequest request) {
        return getGoogleUser(request.token);
    }

    public GoogleUser getGoogleUser(GoogleSignupRequest request) {
        return getGoogleUser(request.token);
    }
}
