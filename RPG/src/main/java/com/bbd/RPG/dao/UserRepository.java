package com.bbd.RPG.dao;

import com.bbd.RPG.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository <User, Long> {
}
