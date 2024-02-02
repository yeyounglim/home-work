package hello.hellospringboot.service;


import hello.hellospringboot.domain.Member;
import hello.hellospringboot.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
//@Commit   //commit 사용시 실제 db에 반영
@Transactional  // 테스트 할때 쿼리 날리고 롤백해서 편리
class MemberServiceIntegrationTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    /**
     * 스프링 컨테이너와 DB까지 연결한 통합 테스트
     */

    @Test
    public void 회원가입() {
        //given
        Member member = new Member();
        member.setName("spring100");

        //when
        Long saveId = memberService.join(member);

        //then
        Member findMember = memberService.findOne(saveId).get();
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    public void 중복_회원_예외() {
        //given
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");

        //when
        memberService.join(member1);
        // 예외 발생 시키기
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> memberService.join(member2));

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원 입니다.");
    }

    @Test
    public void 회원_수_조회() {
        //given

        //when
        Long cnt = memberService.totalCnt();

        //then
        assertThat(cnt).isNotNull(); //그냥 전체회원수 조회 되는지 테스트..
    }
}