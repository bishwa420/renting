package com.example.renting.appuser.service;

import com.example.renting.appuser.model.request.FacebookLoginRequest;
import com.example.renting.appuser.model.request.FacebookSignupRequest;
import com.example.renting.appuser.model.thirdparty.FacebookUser;
import com.example.renting.exception.UnauthorizedException;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.FacebookClient.DebugTokenInfo;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FacebookService {

    private static final Logger log = LoggerFactory.getLogger(FacebookService.class);

    private final String APP_TOKEN = "1530092413856847|zIqheMNwE-YkRBIjwEKAY4ryXHE";

    private String getEmail(String accessToken) {

        try {
            FacebookClient facebookClient = new DefaultFacebookClient(APP_TOKEN, Version.VERSION_3_1);
            DebugTokenInfo info = facebookClient.debugToken(accessToken);

            return info.getUserId() + "@facebook.com";
        } catch (Exception e) {
            log.error("Facebook token verification failed: {}", e.getMessage(), e);
            throw UnauthorizedException.ex("Facebook login failed");
        }
    }

    public FacebookUser getFacebookUser(FacebookLoginRequest request) {

        String email = getEmail(request.token);
        return FacebookUser.of(email);

    }

    public FacebookUser getFacebookUser(FacebookSignupRequest request) {

        String email = getEmail(request.token);
        FacebookClient fbClient = new DefaultFacebookClient(request.token, Version.VERSION_3_1);
        User user = fbClient.fetchObject("me", User.class, Parameter.with("fields", "email, first_name, last_name"));

        return FacebookUser.of(user.getFirstName() + " " + user.getLastName(), email);
    }
}
