package com.ggs.STAT_TurboFetch.service;

import com.ggs.STAT_TurboFetch.client.FtClientImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final FtClientImpl ftClient;

    public List<String> getUsers(String code) {

        return ftClient.getAllUsers(code);
    }
}
