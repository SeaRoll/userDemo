package com.example.userdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class UserDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserDemoApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        // Used to get Bcrypt password encoder to be used for password authentication.
        return new BCryptPasswordEncoder();
    }
}
