package com.example.mztox.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/*ModelMapper를 Spring의 빈으로 등록하여 애플리케이션 전역에서 사용할 수 있도록 설정합니다.
*
* @Configuration 어노테이션이 붙은 클래스는 빈(Bean)을 정의하는 메서드를 포함합니다.
* 이러한 메서드들은 @Bean 어노테이션을 사용하여 빈을 정의합니다.*/
@Configuration
public class ModelMapperConfig {
    //@Bean에 등록
    @Bean
    public ModelMapper modelMapper() {
        //new ModelMapper()를 호출하여 ModelMapper의 새로운 인스턴스를 생성합니다.
        return new ModelMapper();
    }
}
