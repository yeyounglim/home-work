### 스프링 서버에서 유저가 리퀘스트를 보냈을 때 리스폰스 받기 까지 발생하는 일

1. 클라이언트가 브라우저로 웹 서버에 서비스를 요청.
2. http 통신을 위해 tcp 연결. 연결이 성공하면 http request가 나감. (GET, POST 등 메서드, 헤더, 본문 등으로 구성)
3. 웹 서버에 도착.
   - 해당 요청이 동적인지 정적 인지를 판단.
   - 만약 정적인 리소스라면 요청된 파일의 경로를 확인.
   - 동적 요청이면 WAS한테 전달.
4. WAS(web application server)에 도착.
   1. 컨테이너 로드 시 스레드 풀에 스레드를 만든다.
   2. 서블릿 컨테이너가 요청을 받음.
   3. 스레드를 할당하고 그 스레드가 서블릿을 호출.
   4. 필터가 Servlet 이전에 실행(인코딩 변환, 파일 압축, XSS방어 등의 요청에 대한 처리)
   5. DispatcherServlet 객체를 싱글톤으로 생성 후 초기화(처음에 한번만 생성하고 재사용)
   6. `service()` 메서드 실행(http 메서드(get, post등)에 따라 개발자가 구현한 do()메서드 호출)
   7. DispatcherServlet은 적절한 컨트롤러를 선택하는 작업을 HandlerMapping으로 전송. HandlerMapping은 수신 요청 URL에 매핑된 컨트롤러를 선택하고 (선택된 핸들러)와 컨트롤러를 DispatcherServlet에 반환.
   8. DispatcherServlet은 Controller의 비즈니스 로직을 실행하는 작업을 HandlerAdapter로 전송.
   9. HandlerAdapter는 Controller의 비즈니스 로직 프로세스를 호출.
   10. <Interceptor 실행 - 로그인 체크, 권한 체크, 로그 처리, 실행 시간 계산 등의 업무 처리>
   11. <AOP 실행 - 로깅, 트랜잭션, 에러 처리>
   12. Controller는 비즈니스 로직을 실행하고 처리 결과를 Model에 설정한 후 뷰의 논리적 이름을 HandlerAdapter에 반환.
   13. DispatcherServlet은 뷰 이름에 해당하는 뷰를 해석하는 작업을 ViewResolver로 전송. ViewResolver는 뷰 이름에 매핑된 뷰를 반환.
   14. DispatcherServlet은 반환 된 View로 렌더링 프로세스를 전송.
   15. View는 모델 데이터를 렌더링하고 응답을 반환.
   16. 스레드 종료.
   17. 컨테이너가 실행 결과를 웹 서버에 전달.
   18. 컨테이너가 종료되는 경우 서블릿의 `destroy()` 메서드 실행.(서블릿 인스턴스를 GC가 정리)
5. 웹 서버는 클라이언트에게 브라우저로 http response.
6. tcp 연결 해제.

---------------------------------------------------------------------------------

## Servlet

- 클라이언트의 요청을 처리하고 결과를 다시 전송하는 자바 언어로 구현되는 서버 프로그램
- 웹 페이지를 동적으로 생성하여 반환
- 클래스 계층구조 :  Servlet(인터페이스) -> GenericServlet -> HttpServlet
- 요청이 올 때마다 스레드 하나를 생성하여 멀티 스레드로 동작

### 컨테이너

- 서블릿과 JSP같은 웹 서버 애플리케이션들은 동적 컨텐츠를 생성하는 웹 컴포넌트이다.
- 이 웹 컴포넌트를 저장, 메모리 로딩, 서블릿의 생명 주기 관리 등을 수행하는 프로그램

### 서블릿 컨테이너

- 클라이언트의 요청에 따라 서블릿을 수행하는 프로그램(톰캣같은 서블릿을 지원하는 was)
- HTTP 요청과 응답의 흐름을 간단한 메서드 호출로 정의
- 서블릿 생명 주기 관리
- 소켓을 구현해 스트림을 생성하여 클라이언트와 통신(`HttpServletRequest`와 `HttpServletResponse` 객체를 생성)
- 컨테이너 로드시 스레드풀에 스레드를 만든다. 요청이 들어오면 스레드를 할당하고 그 스레드가 서블릿을 호출하여 요청을 수행

### Servlet Lifecycle

![](https://i.imgur.com/IxL9E29.png)

### 서블릿 실행 순서

1. 클라이언트로부터 처리 요청 받기 : 웹서버가 헤더 URI 분석 후 요청 페이지가 서블릿 이면 서블릿 컨테이너에게 넘김.
2. 서블릿 컨테이너는 WEB-INF의 classes나 lib에서 찾아서 실행을 준비
3. 최초 요청여부 판단 : 실행 서블릿객체가 메모리에 없으면 최초 요청으로 판단
4. 서블릿 객체 생성 : 최초 요청이면 서블릿을 클래스 로더가 메모리에 로딩하고 객체 생성. 한번만 생성하는 싱글톤으로 메모리에 올라감
5. init()메서드 실행 : `init()` 메서드를 실행하면 서블릿 객체 초기화. GenericServlet 클래스에 구현되어 있음
6. `service()` 메서드 실행 : 실행하는 서블릿의 요청 순서에 상관없이 클라이언트의 요청이 있을 때마다 실행. 서블릿이 실행될때 자동으로 실행하는 메서드 이므로 반드시 오버라이딩 필요. HttpServlet에서 구현된 `service()` 메서드를 그대로 사용하고 싶을 경우엔 오버라이딩X. (HttpServlet에서get, post등 요청 방식의 정보에 따라 각각 메서드를 호출하게 되어 있음)
7. 위의 단계가 끝나면 서버 에서의 실행이 완료됨. 컨테이너가 실행 결과를 웹 서버에 전달하고 웹 서버는 클라이언트에 응답.

### Spring 

- 자바 플랫폼을 위한 오픈소스 어플리케이션 프레임워크

### Spring Container 

- IoC를 이용하여 Bean 생명 주기를 관리
- IoC와 DI(의존성 주입)을 제공해주는 역할
- Spring Container의 유형 : Bean Factory와 이를 상속한 ApplicationContext 존재

##### IoC (제어의 역전-Inversion of Control):

- 코드의 제어 흐름이 역전되어 객체의 생성과 생명 주기를 프레임 워크나 컨테이너에게 위임
- 객체의 생성과 관리를 외부 컨테이너가 담당

##### DI (의존성 주입-Dependency Injection):

- 객체가 필요로 하는 의존성을 외부에서 주입 받는 디자인 패턴
- 객체 간의 결합도를 낮추고 재사용성을 높이기 위해 사용

### MVC 패턴

-  애플리케이션을 Model, View, Controller 세 가지 영역으로 분리한 디자인 패턴  
   **- Model** : 데이터 요소 (Service객체(서비스 처리), DAO객체(DB처리)로 구분)  
   **- View** : UI 요소 (HTML, CSS, JavaScript, JSP 등)  
   **- Controller** : View에서 클라이언트가 서비스를 요청했을 때 실행. 요청과 서비스 처리 객체를 연결.    

### Spring MVC

- 스프링 내에 존재하는 코어 모듈 프레임 워크

### DispatcherServlet (Spring MVC)

- HTTP 프로토콜을 통해 들어오는 모든 요청을 중앙 집중식으로 처리하는 프론트 컨트롤러 패턴. Spring Container의 제일 앞에서 요청 처리
- Spring MVC의 핵심 요소
- `web.xml` 파일 이나 `@WebServlet` 어노테이션을 통해 접근 가능


![](https://i.imgur.com/xV2Y8f8.png)

### DispatcherServlet 실행 과정

1. DispatcherServlet이 요청을 수신.
2. DispatcherServlet은 적절한 컨트롤러를 선택하는 작업을 HandlerMapping으로 전송. HandlerMapping은 수신 요청 URL에 매핑된 컨트롤러를 선택하고 (선택된 핸들러)와 컨트롤러를 DispatcherServlet에 반환.
3. DispatcherServlet은 Controller의 비즈니스 로직을 실행하는 작업을 HandlerAdapter로 전송.
4. HandlerAdapter는 Controller의 비즈니스 로직 프로세스를 호출.
5. Controller는 비즈니스 로직을 실행하고 처리 결과를 Model에 설정한 후 뷰의 논리적 이름을 HandlerAdapter에 반환.
6. DispatcherServlet은 뷰 이름에 해당하는 뷰를 해석하는 작업을 ViewResolver로 전송. ViewResolver는 뷰 이름에 매핑된 뷰를 반환.
7. DispatcherServlet은 반환된 View로 렌더링 프로세스를 전송.
8. View는 모델 데이터를 렌더링하고 응답을 반환.

#### HandlerMapping

- 클라이언트의 요청을 바탕으로 어떤 Handler(Controller 메소드)를 실행할지 결정

#### HandlerAdapter 

- HandlerAdapter은 controller의 메소드들 중 요청에 맞는 적합한 메소드를 매칭

#### ViewResolver

- 어떤 뷰를 선택할지 결정
- JSP, Thymeleaf등 설정 가능
- Rest API의 json형식은 HandlerAdapter에서 MessageConverter가 동작

### Filter/Interceptor/AOP

![](https://i.imgur.com/OBjK3RN.png)
![](https://i.imgur.com/iYb6nUT.png)
**Filter(필터)**

- 주소로 대상 지정
- Servlet 이전에 실행
- 인코딩 변환, 파일 압축, XSS방어 등의 요청에 대한 처리

**Interceptor(인터셉터)**

- 주소로 대상 지정
- 컨트롤러를 호출하기 전, 후로 실행
- 로그인 체크, 권한 체크, 로그 처리, 실행시간 계산 등의 업무 처리

**AOP(Aspect Orient Programming : 관점 지향 프로그래밍)**

- 세세하게 대상 지정 가능
- 부가적인 기능으로 중간 중간 삽입되어야 할 기능을 분리
- 로깅, 트랜잭션, 에러 처리 등에 사용

https://mossgreen.github.io/  

https://terasolunaorg.github.io/  

https://velog.io/@sweet_sumin   

https://velog.io/@soyeon207  
