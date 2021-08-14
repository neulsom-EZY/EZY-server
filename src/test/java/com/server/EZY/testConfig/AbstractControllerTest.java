package com.server.EZY.testConfig;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
public abstract class AbstractControllerTest {
    protected MockMvc mvc;

    abstract protected Object controller();

    /**
     * MockMvcBuilders 를 활용한 MockMvc 커스텀 메서드
     * 1. addFilter: UTF-8 인코딩을 해주지 않으면 테스트 수행 시 body가 깨진다.
     * 2. alwaysDo: 항상 콘솔에 테스트 결과를 찍어줘라.
     *
     * @author: 전지환
     */
    @BeforeEach
    private void setup(){
        mvc = MockMvcBuilders.standaloneSetup(controller())
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();
    }
}
