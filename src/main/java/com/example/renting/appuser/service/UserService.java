package com.example.renting.appuser.service;

import com.example.renting.appuser.db.entity.User;
import com.example.renting.appuser.db.repo.UserRepository;
import com.example.renting.appuser.model.SignupRequest;
import com.example.renting.exception.ConflictException;
import com.example.renting.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    private void userDoesNotExist(String email) {

        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isPresent()) {
            throw ConflictException.ex("User with email: " + email + " already exists");
        }
    }

    public User getByEmail(String email) {

        Optional<User> userOptional = userRepository.findByEmail(email);
        if(!userOptional.isPresent()) {
            log.error("User with email {} not found", email);
            throw NotFoundException.ex("User not found");
        }
        return userOptional.get();
    }

    public void signup(SignupRequest signupRequest) {

        User user = User.of(signupRequest);

        userDoesNotExist(signupRequest.email);

        user = userRepository.save(user);

        log.info("User with email {} has been stored into DB successfully", user.email);
    }
}
