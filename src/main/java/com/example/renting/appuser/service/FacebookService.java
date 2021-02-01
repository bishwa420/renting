package com.example.renting.appuser.service;

import com.example.renting.exception.UnauthorizedException;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.FacebookClient.DebugTokenInfo;
import com.restfb.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FacebookService {

    private static final Logger log = LoggerFactory.getLogger(FacebookService.class);

    private final String APP_TOKEN = "1530092413856847|zIqheMNwE-YkRBIjwEKAY4ryXHE";

    public String getEmail(String accessToken) {

        try {
            FacebookClient facebookClient = new DefaultFacebookClient(APP_TOKEN, Version.VERSION_3_1);
            DebugTokenInfo info = facebookClient.debugToken(accessToken);
            return info.getUserId() + "@facebook.com";
        } catch (Exception e) {
            log.error("Facebook token verification failed: {}", e.getMessage(), e);
            throw UnauthorizedException.ex("Facebook login failed");
        }
    }
}
