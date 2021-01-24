package com.example.renting.appuser.service;

import com.example.renting.appuser.db.entity.User;
import com.example.renting.appuser.db.repo.UserRepository;
import com.example.renting.appuser.model.*;
import com.example.renting.exception.ConflictException;
import com.example.renting.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    private void userDoesNotExist(String email) {

        Optional<User> userOptional = userRepository.findByEmail(email);

        if(userOptional.isPresent()) {
            throw ConflictException.ex("User with email: " + email + " already exists");
        }
    }

    public User getNotDeletedUserByEmail(String email) {

        Optional<User> userOptional = userRepository.findByEmail(email);
        if(!userOptional.isPresent()) {
            log.error("User with email {} not found", email);
            throw NotFoundException.ex("User not found");
        }
        User user = userOptional.get();
        if(user.isDeleted) {
            throw NotFoundException.ex("User deleted permanently");
        }
        return user;
    }

    public void signup(SignupRequest signupRequest) {

        createUser(signupRequest);
    }

    public UserListResponse getUserList(int page, int limit, String nameLike, String emailLike) {

        if(page < 1) page = 1;
        if(limit < 1 || limit > 100) limit = 10;

        String dataSql = "SELECT u FROM User u";
        String countSql = "SELECT count(u) FROM User u";

        List<String> whereClauseList = new ArrayList<>();
        if(nameLike != null) {
            whereClauseList.add("LOWER(u.name) LIKE '%" + nameLike.toLowerCase() + "%'");
        }
        if(emailLike != null) {
            whereClauseList.add("LOWER(u.email) LIKE '%" + emailLike.toLowerCase() + "%'");
        }

        whereClauseList.add("u.isDeleted = FALSE");

        if(!whereClauseList.isEmpty()) {

            dataSql += " WHERE " + String.join(" AND ", whereClauseList);
            countSql += " WHERE " + String.join(" AND ", whereClauseList);
        }

        dataSql += " ORDER BY u.updatedAt DESC";

        Query dataQuery = entityManager.createQuery(dataSql);
        Query countQuery = entityManager.createQuery(countSql);

        dataQuery.setFirstResult((page - 1) * limit);
        dataQuery.setMaxResults(limit);

        List<User> userList = dataQuery.getResultList();
        int totalCount = (int) (long) countQuery.getSingleResult();

        return UserListResponse.response(userList, totalCount, page, limit);
    }

    public void createUser(CreateUserRequest request) {

        userDoesNotExist(request.email);

        User user = User.of(request);

        user = userRepository.save(user);

        log.info("User with email {} has stored into DB successfully", user.email);
    }

    private User getValidUser(UpdateUserRequest request) {

        Optional<User> userOptional = userRepository.findById(request.id);
        if(!userOptional.isPresent()) {
            throw NotFoundException.ex("User ID: " + request.id + " not found");
        }

        User user = userOptional.get();
        if(user.isDeleted) {
            throw NotFoundException.ex("User is already deleted");
        }

        Optional<User> userWithSameEmailOptional = userRepository.findByEmail(request.email);
        if(!userWithSameEmailOptional.isPresent()) {
            return user;
        }
        User userWithSameEmail = userWithSameEmailOptional.get();
        if(!userWithSameEmail.id.equals(user.id))  {
            throw ConflictException.ex("Another user with same email exists");
        }

        return user;
    }

    public void updateUser(UpdateUserRequest request) {

        User user = getValidUser(request);

        user.update(request);

        userRepository.save(user);

        log.info("User #{} has been updated in DB successfully", user.id);
    }

    public void deleteUser(DeleteUserRequest request) {

        Optional<User> userOptional = userRepository.findByEmail(request.email);
        if(!userOptional.isPresent()) {
            throw NotFoundException.ex("The user with email " + request.email + " not found");
        }

        User user = userOptional.get();
        if(user.isDeleted) {
            throw NotFoundException.ex("The user is already deleted");
        }

        user.isDeleted = true;
        user.updatedAt = LocalDateTime.now();

        userRepository.save(user);
    }
}
