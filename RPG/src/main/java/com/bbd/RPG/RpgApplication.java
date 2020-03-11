package com.bbd.RPG;

import com.bbd.RPG.models.Game;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RpgApplication {
    static public Game game = new Game();

    public static void main(String[] args) {
        SpringApplication.run(RpgApplication.class, args);
    }

}
