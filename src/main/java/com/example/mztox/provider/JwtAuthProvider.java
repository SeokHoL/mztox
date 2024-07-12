package com.example.mztox.provider;

import com.example.mztox.entity.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

//이 클래스는 JWT(Json Web Token)를 생성하고 검증하는 역할을 합니다. 주로 JWT 기반의 인증을 처리하기 위해 사용
@RequiredArgsConstructor
@Component //spring의 빈(Bean)으로 등록
public class JwtAuthProvider {

    //@Value 어노테이션은 Spring의 종속성 주입(Dependency Injection) 기능 중 하나로, 외부 설정 값이나 표현식을 클래스의 필드에 주입하는 데 사용 ->application.properties 에 있음
    @Value("${spring.jwt.secret.at}")
    //atSecretKey: JWT를 서명할 때 사용할 비밀 키입니다. application.properties 파일에서 값을 주입받습니다.
    private String atSecretKey;

    @PostConstruct //@PostConstruct: 빈이 초기화된 후 호출되는 메서드
    //init(): 비밀 키를 Base64로 인코딩합니다. 이렇게 하면 JWT 서명에 사용할 수 있습니다.
    protected void init() {
        atSecretKey = Base64.getEncoder().encodeToString(atSecretKey.getBytes());
    }


    private final UserDetailsService userDetailsService;


    public String createToken(long userPk, String email) {//주어진 사용자 ID와 이메일을 기반으로 JWT 토큰을 생성
        Date now = new Date(); //현재 시간 가져옴
        Date validity = new Date(now.getTime() + (1000L * 60 * 60 * 12)); // 12시간 유효기간

        return Jwts.builder()
                .setSubject(email) //일반적으로 토큰의 소유자, 토큰의 주제를 이메일로 설정
                .claim("userPk", userPk) //데이터 조각으로, 사용자에 대한 정보를 포함, 사용자 ID를 클레임에 추가
                .claim("email", email) //데이터 조각으로, 사용자에 대한 정보를 포함,
                .setIssuedAt(now) //토큰의 발행 시간(iat)을 설정합니다. 여기서는 현재 시간을 발행 시간으로 설정
                .setExpiration(validity) //토큰의 만료 시간(exp)을 설정합니다. 여기서는 12시간 후를 만료 시간으로 설정
                .signWith(SignatureAlgorithm.HS256, atSecretKey) //HS256 알고리즘과 비밀 키(atSecretKey)를 사용하여 토큰에 서명
                .compact(); //최종적으로 JWT 토큰을 생성하고, 문자열로 반환
    }

    /*이 메서드는 JWT 토큰을 파싱하여 사용자 정보를 추출하고, 이를 기반으로 Spring Security의 Authentication 객체를 생성하는 역할을 합니다.
     Authentication 객체는 사용자의 인증 상태와 권한 정보를 포함합니다.
     이 메서드의 목적은 JWT 토큰을 통해 인증된 사용자의 정보를 SecurityContext에 설정하는 것입니다.*/
    public Authentication getAuthentication(String token) {//JWT 토큰에서 사용자 정보를 추출하여 Authentication 객체를 생성
        ////claims에는 토큰에 포함된 사용자 정보가 들어 있습니다.
        Claims claims = Jwts.parser().setSigningKey(atSecretKey).parseClaimsJws(token).getBody();
        //userPk라는 키로 저장된 값을 Long 타입으로 가져옵니다.
        Long userPk = claims.get("userPk", Long.class);
        //email이라는 키로 저장된 값을 String 타입으로 가져옵니다.
        String email = claims.get("email", String.class);
        //사용자 ID와 이메일을 사용하여 CustomUserDetails 객체를 생성
        CustomUserDetails userDetails = new CustomUserDetails(userPk, email);
        //userDetails: 인증된 사용자 정보를 담고 있는 CustomUserDetails 객체입니다.
        //"": 인증에 사용된 자격 증명(예: 비밀번호). 여기서는 이미 인증이 완료된 상태이므로 빈 문자열로 설정합니다.
        //userDetails.getAuthorities(): 사용자의 권한 정보를 반환합니다.
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /*이 메서드는 HTTP 요청에서 JWT 토큰을 추출하는 역할을 합니다.
    주로 클라이언트가 HTTP 요청의 헤더에 포함시킨 JWT 토큰을 서버에서 가져오기 위해 사용됩니다.*/
    public String resolveToken(HttpServletRequest request) {
        //HttpServletRequest 객체의 getHeader 메서드를 호출하여 요청 헤더에서 accesstoken이라는 이름의 헤더 값을 가져옵니다.
        return request.getHeader("accesstoken");
    }

    /*이 메서드는 주어진 JWT 토큰의 유효성을 검증하는 역할을 합니다.
    JWT 토큰의 서명과 만료 시간을 확인하여 토큰이 유효한지 여부를 판단합니다.*/
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(atSecretKey).parseClaimsJws(token);
            /*
              before(new Date()): 만료 시간이 현재 시간보다 이전인지 확인합니다.
              이전이면 true (즉, 토큰이 만료됨), 이후이면 false (즉, 토큰이 유효함)입니다.
              !: 이 결과를 반대로 만듭니다. 토큰이 유효한 경우 true, 토큰이 만료된 경우 false를 반환합니다.
                따라서, return !claims.getBody().getExpiration().before(new Date());는 토큰이 아직 유효한지 확인하는 로직입니다.
                토큰이 유효하면 true, 만료되었으면 false를 반환합니다.*/
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
