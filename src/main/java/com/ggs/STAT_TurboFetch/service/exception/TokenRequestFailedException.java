package com.ggs.STAT_TurboFetch.service.exception;

/*
AccessToken 요청 실패 경우
 */
public class TokenRequestFailedException extends RuntimeException{
    public TokenRequestFailedException(String message) { super(message); }
}
