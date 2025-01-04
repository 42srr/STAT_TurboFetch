package com.ggs.STAT_TurboFetch.client;

import com.ggs.STAT_TurboFetch.config.TokenConfig;
import com.ggs.STAT_TurboFetch.api.dto.Oauth2TokenRequestDto;
import com.ggs.STAT_TurboFetch.api.dto.Oauth2TokenResponseDto;
import com.ggs.STAT_TurboFetch.service.exception.TokenRequestFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FtClientImpl implements FtClient {
    private final TokenConfig tokenConfig;
    private static final String CAMPUS_USER_URI = "https://api.intra.42.fr/v2/cursus_users?filter[campus_id]=69&filter[cursus_id]=21&page=";

    @Override
    public List<String> getAllUsers(String code) {
        Oauth2TokenRequestDto requestDto = new Oauth2TokenRequestDto();
        requestDto.setCode(code);
        requestDto.setGrant_type(tokenConfig.getGrantType());
        requestDto.setClient_id(tokenConfig.getClientId());;
        requestDto.setClient_secret(tokenConfig.getClientSecret());
        requestDto.setRedirect_uri(tokenConfig.getRedirectURI());

        log.info("=== Token Request Dto ===\n{}", requestDto.toString());

        RestTemplate restTemplate = new RestTemplate();

        Oauth2TokenResponseDto responseDto;
        try {
            responseDto = restTemplate.postForObject(
                    tokenConfig.getTokenURI(),
                    requestDto,
                    Oauth2TokenResponseDto.class
            );
        } catch (HttpClientErrorException e) {
            throw new TokenRequestFailedException("Fail to get Access Token");
        }

        log.info("=== token ==={}", responseDto.toString());

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
        return allUsers;
    }
}
