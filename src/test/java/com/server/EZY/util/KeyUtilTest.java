package com.server.EZY.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class KeyUtilTest {

    @Autowired
    private KeyUtil keyUtil;

    @Test
    public void KeyTest() {
        String randomKey = keyUtil.getKey(4);
        System.out.println("randomKey = " + randomKey);
        assertTrue(randomKey!=null);
    }
}
