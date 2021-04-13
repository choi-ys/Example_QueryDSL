package io.example.querydsl.ch01_hello_query_dsl.config;

import com.p6spy.engine.spy.P6SpyOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author : choi-ys
 * @date : 2021-04-09 오후 12:24
 * @Content : p6spy 의존성을 통한 실행 쿼리 출력 MessageFormat설정
 */
@Configuration
public class P6spyLogMessageFormatConfiguration {

    @PostConstruct
    public void setLogMessageFormat() {
        P6SpyOptions.getActiveInstance().setLogMessageFormat(P6spySqlFormatConfiguration.class.getName());
    }
}