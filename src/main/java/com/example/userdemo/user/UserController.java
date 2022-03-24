package com.example.userdemo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody User user) {
        return userService.loginUser(user);
    }

    /*
     * Because formatting is Authorization: Bearer <token>, this must be split
     * */
    @GetMapping("/validate")
    public String validateUser(@RequestHeader("Authorization") String jwt) {
        String cutJwt = jwt.split(" ")[1];
        return userService.validateUser(cutJwt);
    }
}
