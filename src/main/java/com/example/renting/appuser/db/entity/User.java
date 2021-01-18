package com.example.renting.appuser.db.entity;

import com.example.renting.appuser.model.CreateUserRequest;
import com.example.renting.appuser.model.SignupRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder(toBuilder = true)
@Table(name = "app_user")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private static final Logger log = LoggerFactory.getLogger(User.class);

    public enum Status {
        NOT_VERIFIED("NOT_VERIFIED"),
        ACTIVE("ACTIVE"),
        INACTIVE("INACTIVE");

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

    @Column(name = "is_deleted")
    public Boolean isDeleted;

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


    public static User of(SignupRequest signupRequest) {

        User user = new User();
        user.name = signupRequest.name;
        user.email = signupRequest.email;
        user.password = BCrypt.hashpw(signupRequest.password, BCrypt.gensalt());
        user.role = signupRequest.role;
        user.updatedAt = LocalDateTime.now();
        user.setStatus(Status.NOT_VERIFIED);
        user.isDeleted = false;

        return user;
    }

    public static User of(CreateUserRequest request) {

        User user = new User();
        user.name = request.name;
        user.email = request.email;
        user.password = BCrypt.hashpw(request.password, BCrypt.gensalt());
        user.role = request.role;
        user.updatedAt = LocalDateTime.now();
        user.setStatus(Status.NOT_VERIFIED);
        user.isDeleted = false;

        return user;
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
                ", isDeleted=" + isDeleted +
                '}';
    }
}
