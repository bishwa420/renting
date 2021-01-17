package com.example.renting.appuser.service;

import com.example.renting.appuser.db.entity.User;
import com.example.renting.appuser.model.LoginRequest;
import com.example.renting.exception.UnauthorizedException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserService userService;

    private static final String SECRET_KEY_STRING = "7fKkVuuu0FuGy7SUPXk8nAT3d+MxZnBktujv0G3qVax/BOqr3hHPw1SN8XNaKVrX1bkZcLOTlMn2pcWYbGCokA==";
    private SecretKey secretKey;

    @PostConstruct
    private void init() {
        byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY_STRING);
        secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA512");
    }

    private void passwordMatches(String hashPassword, String plainPassword) {

        if(!BCrypt.checkpw(plainPassword, hashPassword)) {
            throw UnauthorizedException.ex("Invalid credentials");
        }
    }

    private String generateToken(User user) {

        Date now = new Date();
        long expirationMillis = now.getTime() + 24*3600*1000L;
        Date expirationTime = new Date(expirationMillis);

        JwtBuilder builder = Jwts.builder();
        builder.setIssuer("ADMIN");
        builder.setId(UUID.randomUUID().toString().replace("-", ""));
        builder.setSubject("Authentication token");
        builder.setAudience("User");
        builder.setIssuedAt(now);
        builder.setExpiration(expirationTime);
        builder.signWith(SignatureAlgorithm.HS512,secretKey);

        return builder.compact();
    }

    public String getToken(LoginRequest request) {

        User user = userService.getByEmail(request.email);
        passwordMatches(user.password, request.password);

        return generateToken(user);
    }
}
