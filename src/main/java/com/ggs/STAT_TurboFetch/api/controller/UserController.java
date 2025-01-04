package com.ggs.STAT_TurboFetch.api.controller;

import com.ggs.STAT_TurboFetch.api.ApiResponse;
import com.ggs.STAT_TurboFetch.api.exception.CodeException;
import com.ggs.STAT_TurboFetch.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public ApiResponse<List<String>> getAllUsers(
            @RequestParam(required = false) String code ) {

        log.info("=== code ===\n{}", code);
        if (code == null)
            throw new CodeException("No code value");

        return ApiResponse.ok(userService.getUsers(code));
    }
}
