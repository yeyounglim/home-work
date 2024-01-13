# <프로세스와 스레드>
# 프로세스
- 실행중인 프로그램(CPU를 할당 받아 명령어를 수행)
- 운영체제의 관리 단위
- 1개 이상의 스레드 보유
- OS가 독립 가상 메모리를 할당

## 프로세스의 메모리 구조
- <span style="background:rgba(140, 140, 140, 0.12)">코드(code)</span> : 소스 코드가 올라가는 영역(기계어). Read-Only
- <span style="background:rgba(140, 140, 140, 0.12)">데이터(data)</span> : static 변수, 전역변수 할당
- <span style="background:rgba(140, 140, 140, 0.12)">힙(heap)</span> : 객체가 생성되는 공간(동적 할당 영역)
- <span style="background:rgba(140, 140, 140, 0.12)">스택(stack)</span> : 함수의 호출(지역변수, 파라미터, 리턴 값 등)이 기록 되며 함수가 끝나면 같이 사라짐. 컴파일 시 영역 크기가 결정. 

## 상태
- 생성(**New**) : 만들어진 상태. 메모리 할당을 해야 한다.
- 준비(**Ready**) : 생성이 완료 되어 실행을 기다리는 상태. CPU가 할당되길 기다리는 중
- 실행(**Running**) : CPU가 할당되어 실행 중인 상태.
- 대기(**Blocked**) : I/O요청 등의 이유로 실행 할 수 없는 상태.
- 종료(**Exit**) : 프로세스 실행이 끝나서 자원을 반납해야 한다.
	
	**New → Ready**  	
	OS가 허락하면 준비 상태가 됨  
	
	**Ready → Running**  	 
	디스패처가 실행 상태로 옮김   

	**Running → Exit**  	
	실제로 종료된 상태  

	**Running → Ready**   	
	주어진 CPU점유 시간이 끝나서 timeout이 된 상태  

	**Running → Blocked**  	
	실행 중 I/O발생 등의 작업이 요청된 상태  

	**Blocked → Ready**  	
	요청이 끝나면 실행을 위해 대기 상태 됨  

## 프로세스 제어 블록 (Process Control Block, PCB)
- OS가 프로세스를 제어하기 위한 상태 정보가 저장된다.
- 옷의 태그 같은 역할. 문맥 교환이 일어날 때 프로세스의 상태를 저장한다.

## 프로세스 스케줄링
: CPU를 프로세스에게 적절하게 할당하기 위한 기법

- 선점형 스케줄링 : 실행 중이어도 다른 프로세스가 CPU점유를 빼앗을 수 있는 기법. 운영체제가 자원을 선점 후 조건에 따라 분배.
	- RR 스케줄링(Round Robin Scheduling)
	- SRTF 스케줄링(Shortest Remaining-Time First Scheduling)
	- 다단계 큐 스케줄링(Multilevel Queue Scheduling)
	- 다단계 피드백 큐 스케줄링(Multilevel Feedback Queue Scheduling)
	- RM 스케줄링(Rate Monotonic Scheduling)
	- EDF 스케줄링(Earliest Deadline First Scheduling)
- 비선점형 스케줄링 : 실행중인 프로세스가 자발적으로 중지될 때까지 실행하는 기법.
	- FCFS 스케줄링(First Come First Served Scheduling)
	- SJF 스케줄링(Shortest Job First Scheduling)
	- HRRN 스케줄링(Highest Response Ratio Next Scheduling)


# 스레드
- 프로세스 내에서 작업을 수행하는 단위
- CPU의 실행 단위
- 프로세스는 자원과 제어로 구분 가능. 제어가 스레드
- 프로세스의 메모리 공유
- 스택 구조의 고유 영역 메모리 보유(TLS)
- 멀티 코어 출시 후 스레드 활용 됨

## 상태
- 생성(NEW) : 스레드가 생성 된 상태
- 실행 대기(RUNNABLE) : 실행 가능한 상태
- 일시 정지(BLOCKED) : 일시적으로 중단 된 상태
- 일시 정지(WAITING) : 다른 스레드의 특정 작업 완료를 기다리는 상태
- 일시 정지(TIMED_WAITING) : 일정 시간동안 기다리는 상태
- 종료(TERMINATED) : 실행이 종료된 상태
스레드의 상태도 프로세스와 비슷하게 동작

## 멀티 스레드 
: 프로세스 내의 흐름이 여러개
### 장점
- 여러 작업을 동시에 처리 가능. 대용량 데이터 분할 병렬 처리. 효율⬆
- 프로세스 보다 문맥 교환이 빠름
- 자원 공유 : 스택을 제외한 메모리를 공유. 효율⬆
- 경제성 : 프로세스 보다 생성/삭제 비용이 적게 든다.  

### 단점
- 동시성 문제 발생
- 복잡성 : 코딩하기 힘듦
- 오버헤드 발생 : 문맥 교환(Context-Switching)이 자주 일어나면 느려짐
- 스레드 간 자원이 공유되어 스레드 하나가 죽으면 다른 스레드에도 영향이 있음

#### 동시성(Concurrency) 문제
: 공유 자원에 동시에 접근 해서 발생하는 문제

- 경쟁 조건(Race Condition) : 여러 프로세스/스레드가 동시에 같은 데이터를 조작할 때 타이밍이나 접근 순서에 따라 결과가 달라질 수 있는 상황
- 교착 상태(Dead Lock) : 둘 이상의 프로세스/스레드가 서로 상대의 자원을 사용하기 위해 대기(자신의 리소스는 해제하지 않음)
- 활동 정지(Live Lock) : 둘 이상의 스레드가 락의 해제와 획득을 무한 반복하는 상태
- 기아 상태(Starvation) : 우선 순위가 낮아서 필요한 자원을 얻지 못하고 대기만 하는 상태

#### 동기화(Synchronization)
: 여러 스레드가  한 객체에 접근해도 일관성 유지

- 여러 프로세스/스레드를 동시에 실행해도 공유 데이터의 일관성을 유지하는 것
- 동시성 상황에서의 문제를 해결하기 위한 매커니즘

#### 임계 영역(Critical Section)
: 한 스레드만 접근 가능한 영역

- 공유 데이터의 일관성을 보장하기 위해 하나의 프로세스/스레드만 진입해서 실행 가능한 영역
- 상호 배제(Mutual Exclusion) : 하나의 자원에는 하나의 스레드만 접근 가능해야 한다
- 진행(Progress) : 임계 영역이 비었으면 자원을 사용할 수 있어야 한다
- 한계 대기(Bounded Waiting) : 계속 기다리면 언젠가는 임계 영역에 진입할 수 있어야 한다

#### 병렬성(Parallelism)
- 물리적으로 동시에 실행 되는 것

#### 동시성(Concurrency)
- 작업을 작은 단위로 쪼개서 처리하여 동시에 실행 하는 것처럼 보이게 하는 것

#### 문맥 교환(Context-Switching)
: 실행 중인 프로세스/스레드의 상태를 저장하고 다음 실행할 스레드의 상태를 불러오는 과정

- 멀티태스킹을 지원하기 위해서 문맥 교환을 한다


## Blocking과 Non-Blocking
: 싱글 스레드 기준  
: 요청 후 행동에 따라 나뉨  
: <font color="#de7802">전체 작업의 흐름 자체를 막는지 안 막는지 여부</font>  

### Blocking
-  I/O작업을 요청한 프로세스나 스레드는 **요청이 완료 될 때까지 블락** 됨(요청 후 기다림)

### Non-Blocking IO
 - I/O작업을 요청한 프로세스나 스레드를 블락 시키지 않고 요청에 대한 **현재 상태를 즉시 리턴**(요청하고 일하다가 간간히 작업 완료를 확인)

## 동기와 비동기
:<font color="#de7802">전체 작업에 대한 순차적인 흐름 유무</font>   

### 동기(synchronous) 
- 여러 프로세스/스레드를 동시에 실행해도 공유 데이터의 일관성을 유지하는 것. 

### 비동기(asynchronous)
- 여러 작업을 동시에 실행하는 방법
- 멀티 스레딩, 논블럭I/O 방식이 비동기식
- 완료를 noti 주거나 callback으로 처리  

## 자바 병렬처리 방법
- Thread
- ExecutorService
- Fork/Join framework : ExecutorService 구현체. 작업을 재귀적으로 분할한 다음 각각 결과를 합치는 방식. 한 작업 대기 큐에 일이 몰리면 놀고있는 스레드가 일을 가져가는 Work Stealing 개념이 포함되어 있음
- PararellStream : 스트림 요소를 스레드 여러개 사용해서 각각 처리>)
