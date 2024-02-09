package hello.hellospringboot;


import hello.hellospringboot.repository.MemberRepository;
import hello.hellospringboot.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 어노테이션으로 등록해 줄 수 있지만
 * 자바 코드로 직접 작성하여 스프링 빈 등록도 가능
 */

@Configuration
public class SpringConfig {

    private final MemberRepository memberRepository;

    @Autowired
    public SpringConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;

    }

    @Bean
    public MemberService memberService(){
        return new MemberService(memberRepository);
    }
    // 여기에 @컴포넌트 안쓰고 로컬캐시를 빈으로 등록해 주려고 했는데 순환..오류

}
