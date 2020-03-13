package com.bbd.RPG.dao;

import java.util.Optional;

import com.bbd.RPG.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository <User, Long> , UserRepositoryCustom{
}
