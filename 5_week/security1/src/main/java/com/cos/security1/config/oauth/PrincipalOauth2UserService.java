package com.cos.security1.config.oauth;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    // 구글로 부터 받은 유저리퀘스트userRequest 데이터에 대한 후처리가 되는 함수
    // 오버라이딩 안해도 되지만 하는이유 PrincipalDetails로 리턴하기위해서
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        System.out.println("getAttgetAttributes = " + super.loadUser(userRequest).getAttributes());
        System.out.println("userRequest.getAccessToken().getTokenValue() = " + userRequest.getAccessToken().getTokenValue());
        System.out.println("userRequest.getClientRegistration() = " + userRequest.getClientRegistration()); //registrationId로 어떤 oAuth로 로그인 했는지 알수 있음

        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 구글 로그인을 완료하면 code를 리턴받음(OAuth의 Client라이브러리)->코드를 통해서 AccessToken요청
        // userRequest에는 AccessToken을 받게 되는 것 까지가 들어있음
        // userRequest 정보로 loadUser 메서드를 호출하여 구글로 부터 회원프로필을 받는다.
        System.out.println("loadUser.oAuth2User.getAttributes() = " + oAuth2User.getAttributes());

        //OAuth로 로그인 했을때 회원이 아니면 회원가입을 진행.
        String provider = userRequest.getClientRegistration().getClientId();//google
        String providerId = oAuth2User.getAttribute("sub");
        String username = provider + "_" + providerId; //이렇게 하면 이름이 중복될 일이 없음 google_107272781327569522562
        String email = oAuth2User.getAttribute("email");
        String role = "ROLE_USER";

        User userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            userEntity = User.builder()
                    .username(username)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .provideId(providerId)
                    .build();
            userRepository.save(userEntity);
        }
        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }
}
