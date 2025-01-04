package com.ggs.STAT_TurboFetch.api;

import com.ggs.STAT_TurboFetch.api.exception.CodeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CodeException.class)
    public ApiResponse<Object> codeException(CodeException e) {
        return ApiResponse.badRequest(e, e.getMessage());
    }
}
