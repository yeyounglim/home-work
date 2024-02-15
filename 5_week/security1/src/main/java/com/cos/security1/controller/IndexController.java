package com.cos.security1.controller;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @GetMapping("/test/login")
    public @ResponseBody String loginTest(Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetails) {// 의존성 주입 //@AuthenticationPrincipal어노테이션으로 세션 정보에 접근 가능.UserDetails타입으로 갖고 있지만 PrincipalDetails에서 implements했기 때문에 사용가능
        System.out.println("================/test/login==================");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();//getPrincipal()은 리턴타입이 오브젝트 타입이기 떄문에 다운캐스팅하여 getUser() 호출. 어노테이션 사용하는것과 같음
        System.out.println("principalDetails.getUser() = " + principalDetails.getUser());

        System.out.println("userDetails = " + userDetails.getUser());
        return "세션 정보 확인하기";
    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOAuthLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User oauthUser) {
        System.out.println("================/test/oauth/login==================");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("oAuth2User.getAttributes() = " + oAuth2User.getAttributes());
        System.out.println("oauthUser.getAttributes() = " + oauthUser.getAttributes());

        return "oAuth2User세션 정보 확인하기";
    }

    @GetMapping({"", "/"})
    public String index() {
        //mustache 사용. 기본폴더는 src/main/resources/
        //뷰리졸버 설정: templates(prefix) , .mustache(surffix)-> 의존성 등록해두면 application.yml에서 생략 가능
        return "index"; //src/main/resources/templates/index.mustache를 찾으러 감. WebMvcConfigurer에서 html로 설정 해 준다
    }

    //일반 로그인을 해도 OAuth로 로그인을 해도 PrincipalDetails로 받을 수 있음
    // @AuthenticationPrincipal 어노테이션은 PrincipalDetailsService나 PrincipalOauth2UserService  메서드 종료 시 만들어짐
    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("principalDetails.getUser() = " + principalDetails.getUser());
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "manager";
    }

    // 로그인 화면
    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    // 회원 가입 화면
    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    // 기본 회원가입
    @PostMapping("/join")
    public String join(User user) {
        System.out.println(user);
        user.setRole("ROLE_USER");

        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);

        user.setPassword(encPassword);

        userRepository.save(user); //비번이 암호화가 안돼서 이 한줄로는 시큐리티로 로그인 불가
        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN") // @Secured 어노테이션으로 간단하게 권한 설정 가능.
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
// data()메소드가 실행되기 직전에 실행됨.Secured와 다르게 여러개 설정 가능 - USER_ROLE, ROLE_USER 형식으로 사용가능.
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "데이터 정보";
    }
}
