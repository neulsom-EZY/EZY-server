package com.server.EZY.exception.user;

import com.server.EZY.exception.user.exception.InvalidAccessException;
import com.server.EZY.exception.user.exception.MemberNotFoundException;
import com.server.EZY.response.result.CommonResult;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exception")
public class MemberExceptionController {

    @GetMapping("/member-not-found")
    public CommonResult userNotFoundException(){ throw new MemberNotFoundException(); }

    @GetMapping("/invalid-access")
    public CommonResult invalidAccessException(){ throw new InvalidAccessException(); }

    @GetMapping("/username-not-found/{username}")
    public CommonResult usernameNotFoundException(@PathVariable String username) { throw new UsernameNotFoundException(username); }


}
