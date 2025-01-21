package com.ggs.STAT_TurboFetch.client;

import com.ggs.STAT_TurboFetch.client.dto.UserDto;
import com.ggs.STAT_TurboFetch.client.dto.UserPersonalDto;
import com.ggs.STAT_TurboFetch.config.TokenConfig;
import com.ggs.STAT_TurboFetch.api.dto.Oauth2TokenRequestDto;
import com.ggs.STAT_TurboFetch.api.dto.Oauth2TokenResponseDto;
import com.ggs.STAT_TurboFetch.service.exception.TokenRequestFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
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
    public List<UserDto> getAllUsers(String code) {
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
        List<UserDto> allUsers = new ArrayList<>();
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

            List<UserDto> parsingResponseList = parsingResponse(body);
            allUsers.addAll(parsingResponseList);
            page++;
        }
        return allUsers;
    }

    private static List<UserDto> parsingResponse(ArrayList<HashMap<String, Object>> body){
        List<UserDto> parsingResponseList = new ArrayList<>();

        for(HashMap<String, Object> data : body){
            Double level = extractLevel(data);

            HashMap<String, Object> user = (HashMap<String, Object>) data.get("user");
            UserPersonalDto userPersonalDto = extractUserField(user);

            String largeImage = extractImageField(user);

            UserDto userDto = setUserDtoField(level, userPersonalDto, largeImage);
            parsingResponseList.add(userDto);
        }
        return parsingResponseList;
    }

    private static UserDto setUserDtoField(Double level, UserPersonalDto userPersonalDto, String largeImage){
        UserDto userDto = new UserDto();
        userDto.setLevel(level);
        userDto.setFt_server_id(userPersonalDto.getFt_server_id());
        userDto.setRole(userPersonalDto.getRole());
        userDto.setIntra_id(userPersonalDto.getIntra_id());
        userDto.setWallet(userPersonalDto.getWallet());
        userDto.setCorrection_point(userPersonalDto.getCorrection_point());
        userDto.setImage(largeImage);
        return userDto;
    }

    private static Double extractLevel(HashMap<String, Object> data){
        return (Double) data.get("level");
    }

    public static UserPersonalDto extractUserField(HashMap<String, Object> user){
        Integer ft_server_id_Value = (Integer) user.get("id");
        int ft_server_id = ft_server_id_Value != null ? ft_server_id_Value : 0;

        String intra_id = (String) user.get("login");
        String role = (String) user.get("kind");

        Integer walletValue = (Integer) user.get("wallet");
        int wallet = walletValue != null ? walletValue : 0;

        Integer correctionPointValue = (Integer) user.get("correction_point");
        int correction_point = correctionPointValue != null ? correctionPointValue : 0;

        return new UserPersonalDto(ft_server_id, intra_id, role, wallet, correction_point);
    }

    private static String extractImageField(HashMap<String, Object> user){
        HashMap<String, Object> image = (HashMap<String, Object>) user.get("image");
        HashMap<String, Object> versions = (HashMap<String, Object>) image.get("versions");
        return (String) versions.get("large");
    }
}
