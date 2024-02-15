#### 서블릿
- 자바 웹 애플리케이션은 요청을 받은 후 항상 응답을 HTTP 프로토콜로 전송한다.
- 브라우저는 HTTP 프로토콜만 이해할 수 있다.
- 반대로 자바 코드는 HTTP 프로토콜을 이해할 수 없다.
- 그래서 자바 코드와 브라우저 사이에 중개자가 존재 하는데 이것이 **서블릿**이다.
- 서블릿 컨테이너 혹은 웹 서버라고 부른다. (톰캣)
- 서블릿 컨테이너는 ServletRequest/ServletResponse로 변환하여 Servlet 메서드에 파라미터로 전달한다.

#### 필터
- 필터는 특수 타입의 서블릿이다.
- 웹 애플리케이션으로 들어오는 모든 요청을 가로 챈다. (실질적인 서블릿 앞에 존재)
- 비즈니스 로직 수행 전 사전 작업을 할 때 사용.
- 이 필터를 사용하여 Spring Security가 보안을 적용한다.

# Spring Security
- 스프링 기반 애플리케이션의 보안을 담당하는 프레임워크
- 서블릿 필터를 기반으로 동작한다.

### 동작 과정
![](https://i.imgur.com/udKlZFn.png)

1. 유저가 로그인 요청 (Http Request)  
2. AuthenticationFilter 에서 UsernamePasswordAuthentication Token 을 생성하여 AuthenticationManager 에 전달  
3. AuthenticationManager 은 등록된 AuthenticationProvider 들을 조회하여 인증 요구  
4. AuthenticationProvider 은 UserDetailService 를 통해 입력받은 아이디에 대한 사용자 정보를 User(DB) 에서 조회  
5. User 에 로그인 요청한 정보가 있는 경우 UserDetails 로 꺼내서 유저 session 생성  
6. 인증이 성공된 UsernameAuthenticationToken 을 생성하여 AuthenticationManager 로 반환  
7. AuthenticationManager 은 UsernameAuthenticationToken 을 AuthenticationFilter 로 전달  
8. AuthenticationFilter 은 전달받은 UsernameAuthentication 을 LoginSuccessHandler 로 전송하고, spring security 인메모리 세션저장소인 SecurityContextHolder 에 저장  
9. 유저에게 session ID 와 응답을 내려줌

##### 용어
- Authentication : 인증. 접근 유저를 확인
- Authorization : 권한 승인->인증된 사용자의 권한을 확인하고 허락
- Principal : 접근 주체
- Credential : 자격 증명, 인증 정보-> 유저 비밀번호


![Imgur](https://i.imgur.com/5ZXNBF7.png)
##### Spring Security Filters
- 각 요청을 가로채고 함께 작동하여 인증이 필요한지 여부를 식별한다. 
- 인증 정보를 확인하여 인증이 필요한 경우 사용자를 로그인 페이지로 이동시킨다.  

##### Authentication
- UsernamePasswordAuthenticationFilter와 같은 필터는 HTTP 요청에서 사용자 이름/비밀번호를 추출하고 인증 유형 객체를 준비한다.  

##### AuthenticationManager
- 필터에서 요청을 받으면 사용 가능한 인증 제공자(authentication providers)에게 사용자 세부 정보의 유효성 검사를 위임한다.  

##### AuthenticationProvider
- 인증을 위해 사용자 세부 정보의 유효성을 검사하는 핵심 로직이 있다.

##### UserDetailsManager/UserDetailsService
- DB/스토리지 시스템에서 사용자 세부 정보를 검색, 생성, 업데이트, 삭제하는 데 사용한다.  

##### PasswordEncoder
- 비밀번호 인코딩 및 해싱 처리 인터페이스.  

##### SecurityContext
![](https://i.imgur.com/1bUhY6v.png)

- 인증 후 인증정보가 저장 된다. 로그인 후 다음 요청을 보낼 때 도움
- ThreadLocal - 스레드의 로컬 저장소



https://velog.io/@soyeon207/SpringBoot-%EC%8A%A4%ED%94%84%EB%A7%81-%EC%8B%9C%ED%81%90%EB%A6%AC%ED%8B%B0%EB%9E%80