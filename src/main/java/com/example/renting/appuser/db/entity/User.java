package com.example.renting.appuser.db.entity;

import com.example.renting.appuser.model.request.CreateUserRequest;
import com.example.renting.appuser.model.request.UpdateUserRequest;
import com.example.renting.appuser.model.thirdparty.ThirdPartyUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder(toBuilder = true)
@Table(name = "app_user")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private static final Logger log = LoggerFactory.getLogger(User.class);

    public enum Status {
        NOT_VERIFIED("NOT_VERIFIED"),
        VERIFIED("VERIFIED");

        private String status;

        Status(String status) {
            this.status = status;
        }

        public String get() {
            return this.status;
        }
    }

    public enum Role {
        ADMIN("ADMIN"),
        REALTOR("REALTOR"),
        CLIENT("CLIENT");

        private String role;

        Role(String role) {
            this.role = role;
        }

        public String get() {
            return this.role;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(name = "name")
    public String name;

    @Column(name = "email")
    public String email;

    @Column(name = "password")
    public String password;

    @Column(name = "role")
    private String role;

    @Column(name = "updated_at")
    public LocalDateTime updatedAt;

    @Column(name = "status")
    private String status;

    @Column(name = "is_suspended")
    public Boolean isSuspended;

    @Column(name = "verification_param")
    public String verificationParam;

    public void setStatus(Status status) {

        this.status = status.get();
    }

    public Status getStatus() {
        return Status.valueOf(this.status);
    }

    public Role getRole() {
        return Role.valueOf(this.role);
    }

    public void setRole(Role role) {
        this.role = role.get();
    }

    @PreUpdate
    @PrePersist
    public void setUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }


    public static User of(CreateUserRequest createUserRequest) {

        User user = new User();
        user.name = createUserRequest.name;
        user.email = createUserRequest.email;
        user.password = BCrypt.hashpw(createUserRequest.password, BCrypt.gensalt());
        user.role = createUserRequest.role;
        user.updatedAt = LocalDateTime.now();
        user.setStatus(Status.NOT_VERIFIED);
        user.verificationParam = UUID.randomUUID().toString().replaceAll("-","");
        user.isSuspended = false;

        return user;
    }

    public static User of(ThirdPartyUser thirdPartyUser, String role) {

        User user = new User();
        user.name = thirdPartyUser.name;
        user.email = thirdPartyUser.email;
        user.role = role;
        user.updatedAt = LocalDateTime.now();
        user.setStatus(Status.NOT_VERIFIED);
        user.verificationParam = UUID.randomUUID().toString().replaceAll("-","");
        user.isSuspended = false;

        return user;
    }


    public void update(UpdateUserRequest request) {

        this.name = request.name;
        this.email = request.email;
        this.role = request.role;
        this.isSuspended = request.doSuspend;
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + "****" + '\'' +
                ", role='" + role + '\'' +
                ", updatedAt=" + updatedAt +
                ", status='" + status + '\'' +
                ", isDeleted=" + isSuspended +
                '}';
    }
}
