package com.ggs.STAT_TurboFetch.api;

import com.ggs.STAT_TurboFetch.api.config.TokenConfig;
import com.ggs.STAT_TurboFetch.api.dto.Oauth2TokenRequestDto;
import com.ggs.STAT_TurboFetch.api.dto.Oauth2TokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
public class DataLoadController {
    private final TokenConfig tokenConfig;

    @GetMapping("/init")
    public String returnAllUsers(
            @RequestParam(required = false) String code ) {

        /*
        1. URL로 직접 코드값 받아와 복붙하기
        todo
        - api호출시 유저 개입 없이 (로그인 화면 입력 제외) 받아올 수 있게 변형

        개발
        1. 일단 .yml로 관리
        2. request parameter로 관리
         */
        System.out.println("=== code ===");
        System.out.println(code);
        if (code == null)
            return "401";


        /*
        2. code값을 통해 token 정보 요청
        todo
        - 비밀번호 코드 노출 없이
        - dto 사용 의문
         */
        // CODE to AccessToken ->

        Oauth2TokenRequestDto requestDto = new Oauth2TokenRequestDto();
        requestDto.setCode(code);
        requestDto.setGrant_type(tokenConfig.getGrantType());
        requestDto.setClient_id(tokenConfig.getClientId());;
        requestDto.setClient_secret(tokenConfig.getClientSecret());
        requestDto.setRedirect_uri(tokenConfig.getRedirectURI());

        System.out.println("=== Token Request Dto ===");
        System.out.println(requestDto.toString());

        RestTemplate restTemplate = new RestTemplate();
        Oauth2TokenResponseDto responseDto = restTemplate.postForObject(
                tokenConfig.getTokenURI(),
                requestDto,
                Oauth2TokenResponseDto.class
        );

        System.out.println("=== token ===");
        System.out.println(responseDto.toString());


        /*
        3. Access token값을 가지고 42api에 데이터 요청
        todo
        - 데이터 파싱
        - 시간 개선
         */
        final String CAMPUS_USER_URI = "https://api.intra.42.fr/v2/cursus_users?filter[campus_id]=69&filter[cursus_id]=21&page=";

        int page = 0;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + responseDto.getAccess_token());
        HttpEntity request = new HttpEntity(headers);
        String allUsers = "";
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
            allUsers += body;
            System.out.println(body.toString());
            page++;
        }
        return allUsers;
    }
}
