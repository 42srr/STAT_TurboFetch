package com.ggs.STAT_TurboFetch.client;

import static org.junit.jupiter.api.Assertions.*;

import com.ggs.STAT_TurboFetch.client.dto.UserPersonalDto;
import java.util.HashMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FtClientImplTest {

    @Test
    @DisplayName("extractUserField 메소드 실행하여 UserPersonalDto객체가 올바르게 반환되는지 확인한다")
    void testExtractUserField() {
        HashMap<String, Object> user = new HashMap<>();
        user.put("id", 174190);
        user.put("login", "yikim2");
        user.put("kind", "admin");
        user.put("wallet", 5);
        user.put("correction_point", 6);

        UserPersonalDto userPersonalDto = FtClientImpl.extractUserField(user);

        assertNotNull(userPersonalDto);
        assertEquals(174190, userPersonalDto.getFt_server_id());
        assertEquals("yikim2", userPersonalDto.getIntra_id());
        assertEquals("admin", userPersonalDto.getRole());
        assertEquals(5, userPersonalDto.getWallet());
        assertEquals(6, userPersonalDto.getCorrection_point());
    }
}