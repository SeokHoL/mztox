package com.example.mztox.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
/*이 클래스는 Spring 애플리케이션에서 외부 구성 파일(예: application.properties)을 로드하고 사용할 수 있도록 설정하는 구성 클래스*/
@Configuration

// @Value 어노테이션 또는 Environment 객체를 통해 다른 빈에서 주입받아 사용할 수 있습니다.
@PropertySource("classpath:application.properties")
public class PropertyConfig {
}