package com.bbd.RPG.dao;

import java.util.Optional;

import com.bbd.RPG.models.User;

import net.minidev.json.JSONObject;

import java.util.List;
/**
 * UserRepositoryCustom
 */
public interface UserRepositoryCustom {

    List<User> findByUsername(String username);
    List<User> findByEmail(String email);
    int updateUser(JSONObject info);

}