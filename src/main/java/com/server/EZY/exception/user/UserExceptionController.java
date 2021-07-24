package com.server.EZY.exception.user;

import com.server.EZY.exception.user.exception.InvalidAccessException;
import com.server.EZY.exception.user.exception.UserNotFoundException;
import com.server.EZY.response.result.CommonResult;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exception")
public class UserExceptionController {

    @GetMapping("/user-not-found")
    public CommonResult userNotFoundException(){ throw new UserNotFoundException(); }

    @GetMapping("/invalid-access")
    public CommonResult invalidAccessException(){ throw new InvalidAccessException(); }

    @GetMapping("/username-not-found")
    public CommonResult usernameNotFoundException() { throw new UsernameNotFoundException("siwony_"); }


}
