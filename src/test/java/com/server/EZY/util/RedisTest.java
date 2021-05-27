package com.server.EZY.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisTest {

    private final RedisUtil redisUtil;

    @Autowired
    public RedisTest(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Test
    public void redisTest() {
        redisUtil.deleteData("EZY");
        redisUtil.setData("EZY", "EZY-test");
        System.out.println(redisUtil.getData("EZY"));
        Assertions.assertThat(redisUtil.getData("EZY")).isEqualTo("EZY-test");
    }



}
