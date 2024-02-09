package hello.hellospringboot.repository;


import hello.hellospringboot.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
//스프링 데이터 JPA 회원 리포지토리
public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository {

    @Override
    Optional<Member> findByName(String name); //JPQL select m from Member m where m.name = ? 과 같음(?)
}
