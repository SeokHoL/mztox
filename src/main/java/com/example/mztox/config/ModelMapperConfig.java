package com.example.mztox.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/*ModelMapper를 Spring의 빈으로 등록하여 애플리케이션 전역에서 사용할 수 있도록 설정합니다.
*/
@Configuration
/*해당 클래스가 하나 이상의 @Bean 메서드를 선언하고 있음을 나타내며,
Spring 컨테이너에서 빈 정의의 소스가 될 것임을 명시합니다.*/
public class ModelMapperConfig {
    //@Bean에 등록
    @Bean
    public ModelMapper modelMapper() {
        //new ModelMapper()를 호출하여 ModelMapper의 새로운 인스턴스를 생성합니다.
        return new ModelMapper();
    }
}
