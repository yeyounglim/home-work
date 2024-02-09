package com.cos.security1.config.auth;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정에서 loginProcessingUrl을 /login으로 설정
// 로그인 요청이 오면 자동으로 UserDetailsService 타입으로 IoC 되어 있는 loadUserByUsername 함수가 실행된다.
@Service
public class PrincipalDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    //시큐리티 세션 (내부에 Authentication(내부에 UserDetails이 들어감))
    // 오버라이딩 안해도 되지만 하는이유 PrincipalDetails로 리턴하기위해서. 리턴되는 타입은 Athentication객체에 저장됨
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // loginForm.html에 있는 인풋의 name이 다르면 안받아짐. SecurityConfig에서 .formLogin.usernameParameter("폼에서 설정한 이름")으로 바꿔줄 수 있음
        System.out.println("PrincipalDetailsService의 username = " + username);
        User userEntitiy = userRepository.findByUsername(username);
        if (userEntitiy != null) {
            return new PrincipalDetails(userEntitiy);
        }
        return null;
    }
}
