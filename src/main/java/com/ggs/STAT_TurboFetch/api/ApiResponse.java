package com.ggs.STAT_TurboFetch.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {
    private HttpStatus httpStatus;
    private int code;
    private String message;
    private T data;

    private ApiResponse(HttpStatus httpStatus, int code, String message, T data) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> ok (T data) {
        return new ApiResponse<>(HttpStatus.OK, HttpStatus.OK.value(), "Success", data);
    }

    public static <T> ApiResponse<T> badRequest (Exception e, String message) {
        return new ApiResponse<>(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), message, null);
    }
}
