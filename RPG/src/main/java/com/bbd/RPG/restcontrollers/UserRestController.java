package com.bbd.RPG.restcontrollers;

import java.util.List;
import java.util.Optional;

import com.bbd.RPG.dao.UserRepository;
import com.bbd.RPG.models.User;
import com.bbd.RPG.services.PlayerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.minidev.json.JSONObject;

@RestController
public class UserRestController {
    @Autowired
    UserRepository userRepository;
    static PlayerService playerService = new PlayerService();

    @Autowired
    private JavaMailSender javaMailSender;

    @RequestMapping("/")
    public String home() {
        return "";
    }

    @PostMapping("/user/forgotpassword")
    public String forgotPassword(@RequestBody JSONObject value) {
        if (value.containsKey("email")) {
            List<User> user = userRepository.findByEmail(value.getAsString("email"));
            System.out.println("Forgot" + value.getAsString("email"));
            if (user.size() > 0) {
                String randompw = getAlphaNumericString(8);
                JSONObject newpword = new JSONObject();
                newpword.appendField("userID", user.get(0).getUserID());
                newpword.appendField("pword", randompw);
                userRepository.updateUser(newpword);
                sendEmail(value.getAsString("email"), "New Password" ,"This is your new password -> " + randompw);
                return "Check your email";
            } else
                return "No such email found";
        } else
            return "please enter an email";
    }

    @DeleteMapping("/user/delete/{userID}")
    public String deleteUser(@PathVariable Long userID) {
        User user = userRepository.getOne(userID);
        userRepository.delete(user);
        return "deleted";
    }

    @PutMapping(path = "/user/update/", consumes = { "application/json" })
    public String UpdateUserInformation(@RequestBody JSONObject values) {
        int check = userRepository.updateUser(values);
        if (check > 0)
            return "Update successful";
        else
            return "Update failed";
    }

    @RequestMapping("/user/request/{userID}")
    public Optional<User> getUserInformation(@PathVariable("userID") long userID) {
        return userRepository.findById(userID);
    }

    @PostMapping("/user/register")
    public String register(@Validated @RequestBody User user) {
        List<User> check = userRepository.findByUsername(user.getUsername());
        if (check.size() == 0) {
            System.out.println(user.toString());
            userRepository.save(user);
            return "success added";
        }
        return "User already exist";
    }

    @PostMapping(path = "user/login", consumes = { "application/json" }, produces = { "application/json" })
    public String login(@RequestBody JSONObject logMeIn) throws Exception {
        if (logMeIn.containsKey("email")) {
            List<User> user = userRepository.findByEmail(logMeIn.getAsString("email"));
            if (user.size() > 0) {
                if (logMeIn.containsKey("pword")) {
                    if (user.get(0).passwordMatch(logMeIn.getAsString("pword")))
                        return String.valueOf(user.get(0).getUserID());
                    else
                        return "Incorrect password";
                } else
                    return "Please enter password";
            } else
                return "No such user";
        }
        return "Please enter email";
    }

    private void sendEmail(String to,String subject, String message ) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);

        msg.setSubject(subject);
        msg.setText(message);

        javaMailSender.send(msg);

    }

    private String getAlphaNumericString(int n) 
    { 
  
        // chose a Character random from this String 
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                                    + "0123456789"
                                    + "abcdefghijklmnopqrstuvxyz"; 
  
        // create StringBuffer size of AlphaNumericString 
        StringBuilder sb = new StringBuilder(n); 
  
        for (int i = 0; i < n; i++) { 
  
            // generate a random number between 
            // 0 to AlphaNumericString variable length 
            int index 
                = (int)(AlphaNumericString.length() 
                        * Math.random()); 
  
            // add Character one by one in end of sb 
            sb.append(AlphaNumericString 
                          .charAt(index)); 
        } 
  
        return sb.toString(); 
    } 
}
