package com.server.EZY.testConfig;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@TestConfiguration
public class QueryDslTestConfig {

    @PersistenceContext
    private EntityManager em;

    @Bean
    public JPAQueryFactory jpaQueryFactory(){
        return new JPAQueryFactory(em);
    }
}
