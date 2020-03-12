package com.bbd.RPG.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userID", updatable = false, nullable = false)
    private long userID;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email can't be blank")
    @NotNull(message = "Email is mandatory")
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @NotBlank
    @NotNull
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    // @JsonIgnore
    @NotBlank
    @NotNull
    @Column(name = "pword", nullable = false)
    private String pword;

    @Column(name = "xp", columnDefinition = "INT default 0")
    private long xp;

    @Column(name = "level")
    private long level = 1;

    // @Override
    public void setPword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String hashedPassword = passwordEncoder.encode(password);
        pword = hashedPassword;
    }

    public boolean passwordMatch(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        return passwordEncoder.matches(password, pword);
    }
}