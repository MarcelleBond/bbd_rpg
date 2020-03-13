package com.bbd.RPG.dao;

import java.util.List;
import java.util.Optional;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import com.bbd.RPG.models.User;

import org.springframework.stereotype.Repository;

import net.minidev.json.JSONObject;

/**
 * UserRepositoryCustomImpl
 */

@Repository
@Transactional
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<User> findByUsername(String username) {
        Query query = entityManager.createNativeQuery("SELECT * FROM user WHERE username = ?", User.class);
        query.setParameter(1, username);

        return query.getResultList();
        // return null;
    }

    @Override
    public List<User> findByEmail(String email) {
        Query query = entityManager.createNativeQuery("SELECT * FROM user WHERE email = ?", User.class);
        query.setParameter(1, email);

        return query.getResultList();
    }

    @Override
    public int updateUser(JSONObject info) {
        try {

            if (!info.containsKey("userID") || info.isEmpty())
                return -1;
            Query query1 = entityManager.createNativeQuery("SELECT * FROM user WHERE userID = ?", User.class);
            List<User> userList = query1.setParameter(1, info.get("userID")).getResultList();
            if (userList.size() == 0)
                return -1;
            User user = userList.get(0);
            String sql = "";
            if (info.containsKey("username")) {
                if (!user.getUsername().equalsIgnoreCase(info.getAsString("username"))
                        && !info.getAsString("username").isEmpty())
                    sql += "username = '" + info.getAsString("username") + "',";
            }
            if (info.containsKey("email")) {
                if (!user.getEmail().equalsIgnoreCase(info.getAsString("email"))
                        && !info.getAsString("email").isEmpty())
                    sql += " email = '" + info.getAsString("email") + "',";
            }
            if (info.containsKey("pword") && !info.getAsString("pword").isEmpty()) {
                user.setPword(info.getAsString("pword"));
                sql += " pword = '" + user.getPword() + "',";
            }
            if (info.containsKey("level") && !info.getAsString("level").isEmpty()) {
                sql += " level = '" + info.getAsString("level") + "',";
            }
            if (info.containsKey("xp") && !info.getAsString("xp").isEmpty()) {
                sql += " xp = '" + info.getAsString("xp") + "',";
            }
            if (sql != "") {
                sql = sql.substring(0, sql.length() - 1);
                String sql2 = "UPDATE user SET " + sql + " WHERE userID = ?";
                Query query = entityManager.createNativeQuery(sql2, User.class);
                query.setParameter(1, info.getAsNumber("userID"));
                return query.executeUpdate();
            } else
                return 0;
        } catch (Exception e) {
            return -1;
        }

    }

}