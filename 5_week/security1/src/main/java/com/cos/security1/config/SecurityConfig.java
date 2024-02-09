package com.cos.security1.config;

// 1. 코드받기(인증) 2.엑세스토큰 (권한생김) 3. 사용자 프로필 정보를 가져오고
// 4-1. 그 정보를 토대로 회원가입을 자동으로 진행 시키기도 함
// 4-2(이메일, 이름, 전번, 아이디) 정보 이외에 추가적인 정보가 필요하면 추가적인 회원가입 창이 나와서 회원가입 진행(쇼핑몰 같은걸 한다면 집주소가 필요함)

import com.cos.security1.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;


@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록 됨
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
//secured 어노테이션 활성화. controller에서 간단히 설정 가능. ,//preAutorize,postAutorize 어노테이션 활성화
public class SecurityConfig {
    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    //해당 메서드의 리턴 되는 오브젝트를 IoC로 등록
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/user/**").authenticated() //user로가면 인증해야 한다
                        .requestMatchers(antMatcher("/manager/**")).hasAnyRole("ADMIN", "MANAGER") //manager로 가면 hasRole의 권한이 있어야 한다(글로벌로 권한설정)
                        .requestMatchers(antMatcher("/admin/**")).hasRole("ADMIN")// admin으로 가면 hasRole의 권한이 있어야 한다
                        .anyRequest().permitAll() //나머지 주소는 모든 권한이 허용되어 있음
                )
                .formLogin(formLogin -> formLogin //기본로그인 화면 안쓰고 로그인화면을 만들어서 등록
                        .loginPage("/loginForm")
                        .loginProcessingUrl("/login") // /login주소가 호출되면 시큐리티가 낚아채서 대신 로그인을 진행함. 컨트롤러에 /login을 만들지 않아도 된다
                        .defaultSuccessUrl("/") //loginForm을 통해 로그인을 하게 되면 /로 보내주는데 특정 페이지를 요청(로그인 안한 채로 권한 페이지 접속)해서 로그인을 하게 되면 로그인 후 특정 페이지로 이동하게 해준다
                        .permitAll()
                )
                .oauth2Login(httpSecurityOAuth2LoginConfigurer -> httpSecurityOAuth2LoginConfigurer
                        .loginPage("/loginForm")
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(principalOauth2UserService)//구글 로그인이 완료된 뒤에 후처리가 필요함. Tip. 코드를 받는게 아니라 엑세스 토큰+사용자 프로필정보를 한번에 받음)
                        )
                );
        return http.build();
    }
}
