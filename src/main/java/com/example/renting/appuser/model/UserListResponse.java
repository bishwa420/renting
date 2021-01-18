package com.example.renting.appuser.model;

import com.example.renting.appuser.db.entity.User;
import com.example.renting.model.Page;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class UserListResponse {

    public static class UserResponse {

        public Long userId;
        public String name;
        public String email;
        public String status;
        public String role;
        public String updatedAt;

        public UserResponse(User user) {

            this.userId = user.id;
            this.name = user.name;
            this.email = user.email;
            this.status = user.getStatus().get();
            this.role = user.getRole().get();
            this.updatedAt = user.updatedAt.format(DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm a"));
        }

        @Override
        public String toString() {
            return "UserResponse{" +
                    "userId=" + userId +
                    ", name='" + name + '\'' +
                    ", email='" + email + '\'' +
                    ", status='" + status + '\'' +
                    ", role='" + role + '\'' +
                    ", updatedAt='" + updatedAt + '\'' +
                    '}';
        }
    }

    public List<UserResponse> userList;
    public Page page;


    public static UserListResponse response(List<User> userList, int totalCount, int page, int limit) {

        UserListResponse userListResponse = new UserListResponse();
        userListResponse.userList = userList
                .stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
        userListResponse.page = Page.of(totalCount, page, limit);
        return userListResponse;
    }

    @Override
    public String toString() {
        return "UserListResponse{" +
                "userList=" + userList +
                ", page=" + page +
                '}';
    }
}
