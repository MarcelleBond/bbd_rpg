package com.bbd.RPG.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userID", updatable = false, nullable = false)
    private long userID;

    @NotBlank
    @NotNull
    @Column(name = "email", unique=true, nullable = false)
    private String email;

    @NotBlank
    @NotNull
    @Column(name = "username", unique=true, nullable = false)
    private String username;

//    @JsonIgnore
    @NotBlank
    @NotNull
    @Column(name = "password", nullable = false)
    private String password;
}
