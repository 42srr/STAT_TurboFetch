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

    @Test
    @DisplayName("extractUserField 잘못된 데이터 형식이 들어올 경우 예외가 발생하는지 확인한다")
    void testExtractUserFieldWithInvalidData() {
        HashMap<String, Object> user = new HashMap<>();
        user.put("id", "invalid_id");
        user.put("login", 12345);
        user.put("kind", null);
        user.put("wallet", "invalid_wallet");
        user.put("correction_point", null);

        assertThrows(ClassCastException.class, () -> {
            FtClientImpl.extractUserField(user);
        });
    }
}