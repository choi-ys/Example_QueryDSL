package io.example.querydsl.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author : choi-ys
 * @date : 2021/04/12 12:22 오후
 * @Content : Spring Data Jpa Auditing 설정 부
 */
@Configuration
@EnableJpaAuditing
public class AuditorConfig {
}