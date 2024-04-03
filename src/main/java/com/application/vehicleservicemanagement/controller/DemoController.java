package com.application.vehicleservicemanagement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {
    @GetMapping("/everyone")
    public String forEveryone() {
        return "Hello, World!";
    }

    @GetMapping("/service-advisor")
    public String forServiceAdvisor() {
        return "Hello, Advisor!";
    }

    @GetMapping("/admin")
    public String forAdmin() {
        return "Hello, Admin!";
    }
}
