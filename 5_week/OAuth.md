# OAuth2.0 (Open AUthorization)
-  다른 애플리케이션의 데이터에 엑세스 할수 있는 권한을 부여하는 보안 표준
- 자유 오픈소스 프로토콜
- 4가지 방식이 있지만 자주쓰는 방식은 AUTHORIZATION CODE GRANT TYPE 이다.

## AUTHORIZATION CODE GRANT TYPE (권한 부여 승인 코드 방식)

![](https://i.imgur.com/o5VP8Vt.png)

유저-클라이언트 서버-인증 서버-리소스 서버로 구성 된다.

1. 유저(리소스 소유자)가 클라이언트 서버에 접속요청
2. 클라이언트 서버가 인증 서버에서 인증서를 만들어서 제공하라 요청
3. 유저가 인증 서버에게 내 인증정보를 달라함
4. 인증 서버는 인증서를 통해 리소스 소유자의 신원이 증명되면 임시 인증코드 발급 후 클라이언트 서버에게 보내줌
5. 클라이언트 서버가 인증 코드를 발급 받았다고 인증 서버에게 알려줌
6. 정보가 일치하면 인증 서버가 클라이언트 서버에게 엑세스 토큰을 발급함
7. 클라이언트 서버는 엑세스 토큰을 가지고 리소스 서버에게 접근해서 리소스를 요청함
8. 토큰 인증이 성공하면 리소스 서버가 요청한 리소스를 클라이언트 서버에 제공함

##### 2~3단계에서 보낼 세부 정보
- client_id - 인증 서버에서 클라이언트 애플리케이션을 식별하는 ID. 클라이언트가 인증 서버에 처음 등록할 때 부여된다.
- redirect_uri - 인증 성공 후 인증 서버가 리디렉션하는 데 필요한 URI. 등록 중에 기본값이 제공된 경우 선택 사항.
- scope - 권한 범위 지정. READ와 같이 클라이언트가 요청하는 액세스 수준을 지정.
- state - CSRF 공격으로부터 보호하기 위한 CSRF 토큰 값.
- response_type - authorization code grant type을 사용하려면 response_type을 'code'로 지정하여 요청해야 한다.

## SNS 로그인
- OAuth2.0 + 서버 인증(세션/쿠키 , 토큰기반 인증)으로 구성
- 구글, 네이버 등은 OAuth2.0 프레임워크를 통해 로그인 API를 제공한다.

##### 동작 과정
![](https://i.imgur.com/Pz1uFnK.png)

##### 장점
- 귀찮은 회원가입을 안해도 된다.
- 사용자들이 권한을 확인하고 허락하기 때문에 안심.


https://tansfil.tistory.com/60