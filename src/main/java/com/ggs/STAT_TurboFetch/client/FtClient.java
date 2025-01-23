package com.ggs.STAT_TurboFetch.client;

import com.ggs.STAT_TurboFetch.client.dto.UserDto;
import java.util.List;

public interface FtClient {
    List<UserDto> getAllUsers(String code);
}
