package com.server.EZY.controller;

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

    @GetMapping("/user")
    public String roleTest() {
        return "Hello~";
    }
}
