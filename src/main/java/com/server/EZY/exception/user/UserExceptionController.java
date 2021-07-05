package com.server.EZY.exception.user;

import com.server.EZY.exception.user.exception.UserNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exception")
public class UserExceptionController {

    @GetMapping("/user-not-found")
    public void userNotFoundException(){
        throw new UserNotFoundException();
    }
}
