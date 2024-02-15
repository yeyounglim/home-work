package com.cos.security1.repository;

import com.cos.security1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

//crud함수를 JpaRepository가 갖고 있음
//JpaRepository를 상속받으면 @Repository 어노테이션을 쓰지 않아도 IoC됨
public interface UserRepository extends JpaRepository<User, Integer> {

    // jpa 쿼리 메서드..
    // findBy까지는 규칙-> Username은 문법
    // select * from user where username = 1?
    public User findByUsername(String username);
}
