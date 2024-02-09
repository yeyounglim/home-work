package com.cos.security1.config.auth;

import com.cos.security1.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행한다.
// 로그인 진행이 완료가되면 시큐리티 세션을 만들어준다.(Security ContextHolder 키값에다 세션저장)
// 시큐리티 세션에 들어갈수 있는 오브젝트가 정해져 있음 => Authentication 타입 객체
// Authentication안에  User 정보가 있어야 한다.
// User오브젝트 타입 = > UserDetails타입 객체
// 시큐리티 세션영역->Authentication->UserDetails(PrincipalDetails)
@Data
public class PrincipalDetails implements UserDetails, OAuth2User { // 일반 로그인 세션 UserDetails / OAuth2로그인 세션 OAuth2User 따로 있기 때문에 둘다 구현해주면 뭘로 로그인 하든 PrincipalDetails로 꺼낼 수 있다

    private User user; //콤포지션
    private Map<String, Object> attributes;


    //OAuth2로 로그인할때 사용하는 생성자
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    //일반 로그인할때 사용하는 생성자
    public PrincipalDetails(User user) {
        this.user = user;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    // 해당 User의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });

        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        // 계정 만료 여부
        return true; //ㄴㄴ
    }

    @Override
    public boolean isAccountNonLocked() {
        // 계정 잠김 여부
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 1년동안 회원이 로그인을 안하면 휴면으로 하기로 할때 사용
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 계정 활성화 여부
        return true;
    }

    @Override
    public String getName() {
        return null;
    }
}
