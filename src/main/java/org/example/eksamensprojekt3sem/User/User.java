package org.example.eksamensprojekt3sem.User;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.eksamensprojekt3sem.Enums.UserRole;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long userId;

    @Column(name = "username")
    @NotBlank(message = "Navn mangler")
    private String username;

    @Column(name = "password")
    @NotBlank(message = "Kodeord mangler")
    private String password;

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Rolle mangler")
    private UserRole userRole;

    protected User() {
    }

    public User(long userId, String username, String password, UserRole userRole) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.userRole = userRole;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
