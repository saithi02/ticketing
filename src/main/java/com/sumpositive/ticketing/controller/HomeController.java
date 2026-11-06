package com.sumpositive.ticketing.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Welcome to Ticketing Management System!";
    }

    @GetMapping("/health")
    public String health() {
        return "Application is running successfully!";
    }
}