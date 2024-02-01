# <캐시(Cache)>
- 데이터나 값을 미리 복사해 놓는 임시 장소
- 값을 저장하여 필요 시 빠르게 가져와 사용할 수 있게 하는 것
- 사용 되었던 데이터는 다시 사용될 확률이 높을 거라 짐작하고 미리 저장해 둔다.
- 병목이 일어날 수 있을만한 지점의 성능 향상을 위해 사용
- 실시간 성이 저하되고 성능이 증가

### 캐시를 사용하기 적절한 데이터
- 단순한 데이터
- 자주 조회되는 데이터
- 잘 변경되지 않는 데이터
- 실시간으로 동기화가 필요하지 않은 데이터
- 복잡한 연산이나 시간이 오래 걸리는 연산

## Cache 사용 구조
![](https://i.imgur.com/0s5t3ev.png)
1. Client로 부터 요청을 받는다.
2. Cache와 작업을 한다.
3. 실제 DB와 작업한다
4. 다시 Cache와 작업한다.

- **look aside cache (Lazy Loading)**
    1. Cache에 Data 존재 유무 확인
    2. Data가 있다면 cache의 Data 사용
    3. Data가 없다면 cache의 실제 DB Data 사용
    4. DB에서 가져온 Data를 Cache에 저장
- **write back**
    1. Data를 Cache에 저장
    2. Cache에 있는 Data를 일정 기간동안 Check
    3. 모여있는 Data를 DB에 저장
    4. Cache에 있는 Data 삭제

## 캐싱 종류

## <span style="background:rgba(74, 82, 199, 0.2)">1 . 응답 캐싱</span>
- 웹 서버의 응답을 메모리에 캐싱한다.
- 애플리케이션 캐시는 로컬 인메모리에 저장되거나(로컬 캐싱) 캐시 서버위에서 실행되는 인메모리 데이터베이스(글로벌 캐싱 : Memcached, Redis)에 저장할 수 있다.

### <span style="background:rgba(240, 200, 0, 0.2)">1) 로컬 캐시(Local Cache)</span>
- 서버의 리소스(Memory, Disk)를 이용한 캐시(Caffeine cache, EHcache)  

##### 장점
- 로컬에서 작동 되기 때문에 데 속도가 빠르다. 
- 서버 어플리케이션과 라이프 사이클을 같이 하므로 사용하기 간편함
- 글로벌 캐시 처럼 네트워크 지연, 단절 문제가 없음
- 아주 간단한 캐시 등은 메모리 기반으로 동작하는 것이 효율적일 수 있음.  

#### 단점
- 휘발성 메모리 → 애플리케이션이 다운되면, 메모리 데이터는 사라짐
- 단일 서버 인스턴스에 캐시 데이터를 저장하기 때문에 서버가 여러대로 클러스터링 되어있는 경우, 동기화가 되지 않는 현상
- 캐시 데이터가 커질수록 애플리케이션의 메모리가 부족해지는 현상 (애플리케이션 성능 저하)

##### 1. EhCache
- EhCache는 Spring에서 간단하게 사용할 수 있는 대표적인 Java 기반 오픈 소스 캐시 라이브러리
- 분산 처리, Cache Listener, OffHeap 등의 기능이 있다.
- 분산 캐시를 사용할 때 각각의 애플리케이션의 캐시를 Terracotta Server를 활용하여 동기화 가능.
- Terracotta Server는 각 캐시 노드들의 허브(Hub) 역할을 하는 분산 캐시 서버. 
- EhCache와 Terracotta Server를 결합하여 캐시 노드들 간의 변경 내용을 공유하고 동기화

##### 2. Guava 캐시
- 구글에서 제공하는 자바 캐시 라이브러리 
- 키-값(Key-Value) 형태의 데이터 구조로 표현되며, 간단한 코드를 통해 캐시 크기, 캐시 유효 기간, 데이터 로딩 방법, 데이터 갱신 방법 등을 제어 가능

##### 3. Caffeine 캐시
- 고성능의 최적의 캐싱 라이브러리 (high performance, near optimal caching library)
- Guava 캐시와 ConCurrentHashMap을 개선한 ConcurrentLinkedHashMap을 바탕으로 구현된 캐시

### <span style="background:rgba(240, 200, 0, 0.2)">2) 글로벌 캐시(Global Cache)</span>
- 여러 서버에서 Cache Server에 접근하여 사용하는 캐시  


##### 장점
- 서버 복제 지원 (Replication)
- 데이터 분산 저장(Sharding)
- 트랜잭션 지원
- 별도의 Cache Server를 이용하기 때문에 서버 간 데이터 공유가 쉽다.
- 확장성이 좋다.  

##### 단점
- 네트워크 트래픽으로 인해 로컬 캐시 보다는 느리다.
- 네트워크 지연, 단절 문제가 있다.

#### Redis
데이터 영속성을 지원하는 In-memory 저장소 (스냅샷 기능을 통해 Disk에 캐시 데이터를 저장할 수 있기 때문)
###### 특징
- Redis는 List, Set, Sorted Set, Hash 등과 같은 **Collection을 지원**
- Race condition에 빠질 수 있는 것을 방지함
    - **Redis는 Single Thread** - 병목 현상이 발생할 수 있다.
    - 따라서 Atomic 보장
- **persistence를 지원**하여 서버가 꺼지더라도 다시 데이터를 불러들일 수 있다.  

##### Redis의 주요 사용처
- Remote Data Store
    - 여러 서버의 Data 공유를 위해 사용될 수 있음.
    - 특히, Redis의 경우 Single Thread 이므로 Race Condition 발생 가능성이 낮다는 것을 활용할 필요가 있을 경우
- 인증 토큰 개발
- Ranking Board (Sorted Set)
- 유저 API Limit
- Job Queue

## <span style="background:rgba(74, 82, 199, 0.2)">2. 데이터베이스 캐싱</span>
- 데이터베이스 쿼리는 데이터베이스 서버에서 수행되기 때문에 속도가 느려지고 부하가 몰릴 수 있다.
- 결과값을 데이터베이스에 캐싱함으로써 응답 시간을 향상시킬 수 있다.
- 대다수의 데이터베이스 서버는 최적화된 캐싱을 위한 기능을 기본적으로 지원한다.

## <span style="background:rgba(74, 82, 199, 0.2)">3. 웹 캐시</span>
##### HTTP 헤더를 통한 브라우저 캐싱
- 모든 브라우저는 HTML, JS, CSS, 이미지와 같은 파일들을 임시 저장을 위해 HTTP 캐시를 제공

##### Proxy 캐싱
- 프록시 서버 자체에 콘텐츠를 저장하여 웹 서비스가 해당 리소스를 더 많은 사용자와 공유할 수 있도록 하는 프록시 서버의 기능
	- 프록시 : 클라이언트와 서버 사이에 대리로 통신을 수행하는 것
	- 프록시 서버 : 클라이언트와 서버 사이에서 중계 기능을 하는 서버  

##### Gateway 캐싱
- 서버 앞 단에 설치되어 요청에 대한 캐쉬 및 효율적인 분배를 통해 가용성, 신뢰성, 성능등을 향상



https://souljit2.tistory.com/72#3  

https://developer-jiing.tistory.com/50  

https://kk-programming.tistory.com/83  

https://sabarada.tistory.com/103  

https://medium.com/uplusdevu/%EB%A1%9C%EC%BB%AC-%EC%BA%90%EC%8B%9C-%EC%84%A0%ED%83%9D%ED%95%98%EA%B8%B0-e394202d5c87#1735
