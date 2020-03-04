package com.bbd.RPG.restcontrollers;

import com.bbd.RPG.dao.UserRepository;
import com.bbd.RPG.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserRestController {
    @Autowired
    UserRepository userRepository;

    @RequestMapping("/")
    public String home()
    {
        return "";
    }

    @DeleteMapping("/user/delete/{userID}")
    public String deleteUser(@PathVariable Long userID)
    {
        User user = userRepository.getOne(userID);
        userRepository.delete(user);
        return "deleted";
    }

    @PostMapping(path="/user/add/",consumes= {"application/json"})
    public User addUser(@RequestBody User user)
    {
        userRepository.save(user);
        return user;
    }

    @PutMapping(path="/user/update/",consumes= {"application/json"})
    public User saveOrUpdateAlien(@RequestBody User user)
    {
        userRepository.save(user);
        return user;
    }

    @RequestMapping("/user/request/{userID}")
    public Optional<User> getAlien(@PathVariable("userID")long userID)
    {
        return userRepository.findById(userID);
    }
}
