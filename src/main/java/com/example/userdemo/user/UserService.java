package com.example.userdemo.user;

import com.example.userdemo.UserDemoApplication;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class UserService {

    @Value("${SECRET_KEY}") // value taken from application.properties
    private String secret;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * User registration
     */
    public String createUser(User user) {
        logger.info("Trying to create user with " + user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "success";
    }

    /**
     * User Login
     */
    public String loginUser(User requestUser) {
        logger.info("User " + requestUser.getUsername() + " trying to log in.");
        User user = userRepository.findUserByUsername(requestUser.getUsername()).orElseThrow(() -> new IllegalStateException("User with that username does not exist"));
        if(passwordEncoder.matches(requestUser.getPassword(), user.getPassword())) {
            return Jwts.builder()
                    .setSubject(user.getUsername())
                    .setExpiration(new Date(System.currentTimeMillis() + 1800000))
                    .signWith(SignatureAlgorithm.HS256, secret.getBytes(StandardCharsets.UTF_8))
                    .compact();
        } else {
            throw new IllegalStateException("invalid password");
        }
    }

    /**
     * Get user details
     * @param jwt jwt string
     * @return user details
     */
    public User getDetail(String jwt) {
        Long id = Long.valueOf(validateUser(jwt));
        return userRepository.findById(id).orElseThrow(() -> new IllegalStateException("Server error"));
    }

    /**
     * Returns username if jwt is valid
     */
    public String validateUser(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(jwt).getBody();
        return claims.getSubject();
    }
}
