package com.ggs.STAT_TurboFetch.service;

import com.ggs.STAT_TurboFetch.client.FtClientImpl;
import com.ggs.STAT_TurboFetch.client.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final FtClientImpl ftClient;

    public List<UserDto> getUsers(String code) {

        return ftClient.getAllUsers(code);
    }
}
