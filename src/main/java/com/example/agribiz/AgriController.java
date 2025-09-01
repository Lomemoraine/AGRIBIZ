package com.example.agribiz;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AgriController {
    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/req/signup")
    public String signup() {
        return "signup";
    }


}
