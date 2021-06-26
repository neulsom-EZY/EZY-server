package com.server.EZY.model.user.controller;

import com.server.EZY.model.user.dto.PasswordChangeDto;
import com.server.EZY.model.user.dto.PhoneNumberDto;
import com.server.EZY.model.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/user")
public class CertifiedUserController {

    private final UserService userService;

    @PostMapping("/phoneNumber")
    @ResponseStatus( HttpStatus.OK )
    public Boolean validPhoneNumber(@Valid @RequestBody PhoneNumberDto phoneNumberDto) {
        return userService.validPhoneNumber(phoneNumberDto);
    }

    @PutMapping("/pwd-change")
    @ResponseStatus( HttpStatus.OK )
    public String changePassword(@Valid @RequestBody PasswordChangeDto passwordChangeDto) {
        return userService.changePassword(passwordChangeDto);
    }
}
