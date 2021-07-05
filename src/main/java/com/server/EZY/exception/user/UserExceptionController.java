package com.server.EZY.exceptionAdvice.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exception")
public class UserExceptionController {

    @GetMapping("/user-not-found")
    public void userNotFasdfeafound(){
        throw new UserNotFoundException();
    }
}
