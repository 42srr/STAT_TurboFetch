package com.ggs.STAT_TurboFetch.api;

import com.ggs.STAT_TurboFetch.api.dto.Oauth2TokenRequestDto;
import com.ggs.STAT_TurboFetch.api.dto.Oauth2TokenResponseDto;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class MainController {
    @GetMapping("/init")
    public void returnAllUsers() {
        /*
        todo
        1. Auth로 내 access token 받아오기
        2. 받아온 access token으로 42api에 데이터 요청해서 받아오기
        3. 받아온 데이터 파싱 후 리턴
         */

        /*
        1. URL로 직접 코드값 받아와 복붙하기
        todo
        - api호출시 유저 개입 없이 (로그인 화면 입력 제외) 받아올 수 있게 변형
         */
        final String CODE = "6658a2a6367047a8dfc20a002d49f430938fbc3fb132fedc6363b7e1a26990b3";

        /*
        2. code값을 통해 token 정보 요청
        todo
        - 비밀번호 코드 노출 없이
        - dto 사용 의문
         */
        // CODE to AccessToken ->
        String TOKEN_URI = "https://api.intra.42.fr/oauth/token";

        String grant_type = "authorization_code";
        String client_id = "u-s4t2ud-7092e7912adc2ca54852261d6738a4ae005f7d3c715b755631600dbe82c154c7";
        String client_secret = "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";
        String redirect_uri = "http://localhost:8080/login/oauth2/code/42";

        Oauth2TokenRequestDto requestDto = new Oauth2TokenRequestDto();
        requestDto.setCode(CODE);
        requestDto.setGrant_type(grant_type);
        requestDto.setClient_id(client_id);;
        requestDto.setClient_secret(client_secret);
        requestDto.setRedirect_uri(redirect_uri);


        RestTemplate restTemplate = new RestTemplate();
        Oauth2TokenResponseDto responseDto = restTemplate.postForObject(
                TOKEN_URI,
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
            System.out.println(body.toString());
            page++;
        }

    }
}
