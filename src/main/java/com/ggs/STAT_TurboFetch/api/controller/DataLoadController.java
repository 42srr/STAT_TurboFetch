package com.ggs.STAT_TurboFetch.api.controller;

import com.ggs.STAT_TurboFetch.api.ApiResponse;
import com.ggs.STAT_TurboFetch.api.config.TokenConfig;
import com.ggs.STAT_TurboFetch.api.dto.Oauth2TokenRequestDto;
import com.ggs.STAT_TurboFetch.api.dto.Oauth2TokenResponseDto;
import com.ggs.STAT_TurboFetch.api.exception.CodeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DataLoadController {

    private static final String CAMPUS_USER_URI = "https://api.intra.42.fr/v2/cursus_users?filter[campus_id]=69&filter[cursus_id]=21&page=";
    private final TokenConfig tokenConfig;

    @GetMapping("/users")
    public ApiResponse<List<String>> returnAllUsers(
            @RequestParam(required = false) String code ) {

        /*
        1. URL로 직접 코드값 받아와 복붙하기
        todo
        - api호출시 유저 개입 없이 (로그인 화면 입력 제외) 받아올 수 있게 변형

        개발
        1. 일단 .yml로 관리
        2. request parameter로 관리
         */
        log.info("=== code ===\n"+code);
        if (code == null)
            throw new CodeException("No code value");

        /*
        2. code값을 통해 token 정보 요청
        todo
        - 비밀번호 코드 노출 없이
        - dto 사용 의문
         */
        Oauth2TokenRequestDto requestDto = new Oauth2TokenRequestDto();
        requestDto.setCode(code);
        requestDto.setGrant_type(tokenConfig.getGrantType());
        requestDto.setClient_id(tokenConfig.getClientId());;
        requestDto.setClient_secret(tokenConfig.getClientSecret());
        requestDto.setRedirect_uri(tokenConfig.getRedirectURI());

        log.info("=== Token Request Dto ===\n" + requestDto.toString());

        RestTemplate restTemplate = new RestTemplate();
        Oauth2TokenResponseDto responseDto = restTemplate.postForObject(
                tokenConfig.getTokenURI(),
                requestDto,
                Oauth2TokenResponseDto.class
        );

        log.info("=== token ===\n" + responseDto.toString());

        /*
        3. Access token값을 가지고 42api에 데이터 요청
        todo
        - 데이터 파싱
        - 시간 개선
         */
        int page = 0;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + responseDto.getAccess_token());
        HttpEntity request = new HttpEntity(headers);
        List<String> allUsers = new ArrayList<>();
        while (true) {
            RestTemplate requestUser = new RestTemplate();
            ResponseEntity<ArrayList> response = requestUser.exchange(
                    CAMPUS_USER_URI + page,
                    HttpMethod.GET ,
                    request,
                    ArrayList.class
            );

            ArrayList<HashMap<String, Object>> body = response.getBody();

            if (body.isEmpty()) break;
            allUsers.add(body.toString());
            page++;
        }
        log.info("=== all Users ===\n" + allUsers.toString());
        return ApiResponse.ok(allUsers);
    }
}
