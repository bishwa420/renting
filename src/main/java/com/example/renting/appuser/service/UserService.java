package com.example.renting.appuser.service;

import com.example.renting.appuser.db.entity.User;
import com.example.renting.appuser.db.repo.UserRepository;
import com.example.renting.appuser.model.SignupRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;


@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    private void init() {

        String regexp = "^([_a-zA-Z0-9-]+(\\\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\\\.[a-zA-Z0-9-]+)*(\\\\.[a-zA-Z]{1,6}))?$";
        regexp.matches("bishwa@gmail.com");
    }

    public void signup(SignupRequest signupRequest) {

        User user = User.of(signupRequest);

        user = userRepository.save(user);

        log.info("User with email {} has been stored into DB successfully", user.email);
    }
}
