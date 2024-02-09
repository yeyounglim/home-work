package hello.hellospringboot.service;


import hello.hellospringboot.domain.Member;
import hello.hellospringboot.repository.MemberRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static hello.hellospringboot.LocalCache.writeCache;


@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    /**
    new로 생성하지 않고 외부에서 넣어주게(DI) 만듦
    */

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 회원 가입
     */
    public Long join(Member member) {
        //이름이 같은 회원X
        validateDuplicateMember(member);    // 중복 회원 검증
        memberRepository.save(member);      //검증 후 저장
        writeCache("totCntRefresh",true);    //회원 추가하면 캐시 변경여부 true
        return member.getId();
    }
    /**
     * 회원 중복 검사(이름 기준)
     */
    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원 입니다.");
                });
    }

    /**
     * 전체 회원 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long memberId){
        return memberRepository.findById(memberId);
    }
    /**
    * 로컬 캐시 테스트용 전체 회원 수 조회
    * */
    public Long totalCnt(){
        return memberRepository.countBy();
    }
}
