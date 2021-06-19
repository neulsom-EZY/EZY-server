package com.server.EZY.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class ControllerTest {

    @GetMapping("/test")
    public String testTest() {
        return "EZY-test";
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/user")
    public String roleTest() {
        return "Hello~";
    }
}
