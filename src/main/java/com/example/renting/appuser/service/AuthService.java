package com.example.renting.appuser.service;

import com.example.renting.appuser.db.entity.User;
import com.example.renting.appuser.model.LoginRequest;
import com.example.renting.exception.RentalException;
import com.example.renting.exception.UnauthorizedException;
import io.jsonwebtoken.*;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

import static java.lang.System.currentTimeMillis;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserService userService;

    private static final String SECRET_KEY_STRING = "7fKkVuuu0FuGy7SUPXk8nAT3d+MxZnBktujv0G3qVax/BOqr3hHPw1SN8XNaKVrX1bkZcLOTlMn2pcWYbGCokA==";

    private SecretKey secretKey;

    private Map<String, Object> ROLE_ADMIN;
    private Map<String, Object> ROLE_CLIENT;
    private Map<String, Object> ROLE_REALTOR;


    @PostConstruct
    private void init() {
        byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY_STRING);
        secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA512");

        ROLE_ADMIN = new HashMap<>();
        ROLE_CLIENT = new HashMap<>();
        ROLE_REALTOR = new HashMap<>();

        ROLE_ADMIN.put("role", User.Role.ADMIN.get());
        ROLE_CLIENT.put("role", User.Role.CLIENT.get());
        ROLE_REALTOR.put("role", User.Role.REALTOR.get());
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

        claims.putAll(getRoleMap(user));

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY_STRING)
                .compact();
    }

    public String getToken(LoginRequest request) {

        User user = userService.getNotDeletedUserByEmail(request.email);
        passwordMatches(user.password, request.password);

        return generateToken(user);
    }

    public User.Role getUserRole(String token) {

        if(token == null) {
            throw UnauthorizedException.ex("Token is missing");
        }

        try {

            Jws<Claims> map = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            Claims claims = map.getBody();
            String email = claims.getAudience();

            User.Role role = User.Role.valueOf((String) claims.get("role"));

            log.info("The token received from user: {} with role: {}", email, role.get());

            return role;
        } catch (SignatureException | MalformedJwtException e) {
            throw UnauthorizedException.ex("Token is invalid");
        } catch (ExpiredJwtException e) {
            throw UnauthorizedException.ex("Token has expired");
        }
    }
}
