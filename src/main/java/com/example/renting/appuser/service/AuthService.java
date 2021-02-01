package com.example.renting.appuser.service;

import com.example.renting.appuser.db.entity.User;
import com.example.renting.appuser.model.FacebookLoginRequest;
import com.example.renting.appuser.model.GoogleLoginRequest;
import com.example.renting.appuser.model.LoginRequest;
import com.example.renting.appuser.model.LoginResponse;
import com.example.renting.exception.ForbiddenException;
import com.example.renting.exception.RentalException;
import com.example.renting.exception.UnauthorizedException;
import com.example.renting.model.UserInfo;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import io.jsonwebtoken.*;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

import static java.lang.System.currentTimeMillis;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserService userService;

    @Autowired
    private FacebookService facebookService;

    private static final String APP_SECRET_KEY_STRING = "7fKkVuuu0FuGy7SUPXk8nAT3d+MxZnBktujv0G3qVax/BOqr3hHPw1SN8XNaKVrX1bkZcLOTlMn2pcWYbGCokA==";
    private static final String GOOGLE_CLIENT_ID = "758898908443-kvlhgb8bpbtfs0jam1kq6i9m4bc1vst5.apps.googleusercontent.com";

    private SecretKey appOwnSecretKey;

    private Map<String, Object> ROLE_ADMIN;
    private Map<String, Object> ROLE_CLIENT;
    private Map<String, Object> ROLE_REALTOR;


    @PostConstruct
    private void init() {
        byte[] decodedKey = Base64.getDecoder().decode(APP_SECRET_KEY_STRING);
        appOwnSecretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA512");

        ROLE_ADMIN = new HashMap<>();
        ROLE_CLIENT = new HashMap<>();
        ROLE_REALTOR = new HashMap<>();

        ROLE_ADMIN.put("role", User.Role.ADMIN.get());
        ROLE_CLIENT.put("role", User.Role.CLIENT.get());
        ROLE_REALTOR.put("role", User.Role.REALTOR.get());

        FacebookClient fbClient = new DefaultFacebookClient("1530092413856847|zIqheMNwE-YkRBIjwEKAY4ryXHE", Version.VERSION_3_1);
        FacebookClient.DebugTokenInfo info = fbClient.debugToken("EAAVvnGbNLE8BAAzLbkHZAEC5apPvyA9OiPYlh9AZA2bGqenmWKAc3tZAbW1MfZAZBI9T3pOpZBCmSL1EzM1gOUUgI6YfaHxvybVgcyNPG7h29uAZARbNOc8G9nc7EkooYLFjOWYak1kv58EtDh6CugLe1W38fZAUPxtDlcEXnBGYRrdyXYQyj77a");
        log.info("info");
    }

    private Map<String, Object> getRoleMap(User user) {

        switch (user.getRole()) {

            case ADMIN:
                return ROLE_ADMIN;
            case CLIENT:
                return ROLE_CLIENT;
            case REALTOR:
                return ROLE_REALTOR;
            default:
                throw RentalException.internal();
        }
    }

    private void passwordMatches(String hashPassword, String plainPassword) {

        if(!BCrypt.checkpw(plainPassword, hashPassword)) {
            throw UnauthorizedException.ex("Invalid credentials");
        }
    }

    private String generateToken(User user) {

        Claims claims = Jwts.claims()
                .setId(UUID.randomUUID().toString().replaceAll("-", ""))
                .setSubject("Authentication token")
                .setExpiration(new Date(currentTimeMillis() + 24*3600*1000L))
                .setIssuedAt(new Date())
                .setAudience(user.email);

        Map<String, Object> map = getRoleMap(user);
        map.put("userId", user.id);
        claims.putAll(map);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, APP_SECRET_KEY_STRING)
                .compact();
    }

    public LoginResponse getToken(LoginRequest request) {

        User user = userService.getNotSuspendedActiveUser(request.email);
        if(!user.getStatus().equals(User.Status.VERIFIED)) {
            throw ForbiddenException.ex("Please verify your email first");
        }

        passwordMatches(user.password, request.password);

        String token = generateToken(user);

        return LoginResponse.of(token, user);
    }

    public LoginResponse getTokenForGoogleLogin(GoogleLoginRequest request) {

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                .build();

        try {
            GoogleIdToken googleIdToken = verifier.verify(request.token);
            if(googleIdToken == null) {
                throw UnauthorizedException.ex("Google login failed");
            }

            Payload payload = googleIdToken.getPayload();
            String email = payload.getEmail();

            User user = userService.getNotSuspendedActiveUser(email);

            String token = generateToken(user);

            return LoginResponse.of(token, user);

        } catch (GeneralSecurityException | IOException e) {
            log.error("Google ID token verification exception: {}", e.getMessage(), e);
            throw UnauthorizedException.ex("Invalid Google token");
        }

    }

    public LoginResponse getTokenForFacebookLogin(FacebookLoginRequest request) {

        String email = facebookService.getEmail(request.token);
        User user = userService.getNotSuspendedActiveUser(email);

        String token = generateToken(user);
        return LoginResponse.of(token, user);
    }

    public void verifyUser(String verificationParam) {

        userService.verifyUser(verificationParam);
    }

    public UserInfo getUserInfo(String token) {

        if(token == null) {
            throw UnauthorizedException.ex("Token is missing");
        }

        Claims claims = getClaims(token);
        Long userId = ((Integer) claims.get("userId")).longValue();
        String role = (String) claims.get("role");

        return UserInfo.of(userId, role);
    }

    private Claims getClaims(String token) {

        try {
            Jws<Claims> map = Jwts.parser().setSigningKey(appOwnSecretKey).parseClaimsJws(token);
            return map.getBody();
        } catch (SignatureException | MalformedJwtException e) {
            throw UnauthorizedException.ex("Token is invalid");
        } catch (ExpiredJwtException e) {
            throw UnauthorizedException.ex("Token has expired");
        }
    }

    public User.Role getUserRole(String token) {

        if(token == null) {
            throw UnauthorizedException.ex("Token is missing");
        }

        Claims claims = getClaims(token);
        String email = claims.getAudience();

        User.Role role = User.Role.valueOf((String) claims.get("role"));

        log.info("The token received from user: {} with role: {}", email, role.get());

        return role;
    }
}
