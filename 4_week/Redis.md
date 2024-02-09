# <Redis>
- In-memory 데이터 스토어 - 전원 날라가면 저장 안됨
- 캐시 이외의 용도로 사용할 경우 데이터 백업이 필요
- 이벤트 루프(Event Loop)를 이용하여 요청을 수행
- 실제 명령에 대한 작업(Task)은 커널 레벨에서 멀티플렉싱(Multiplexing)을 통해 처리하여 동시성을 보장
- 유저 레벨에서는 싱글 스레드로 동작하지만, 커널 I/O 레벨에서는 스레드 풀을 이용


## Redis 지속성(Persistence)
-  SSD와 같은 내구성 있는 스토리지에 데이터를 기록하는 것

#### RDB (Redis Database) 
: 값 저장  
- 저장 당시의 메모리를 그대로 스냅 샷 형태로 파일로 저장. 
- 바이너리 형태로 저장.
- 데이터 셋을 버전 별로 저장하여 재해 복구 백업 용도로 적합하나 Redis의 동작이 멈추는(정전) 경우 저장되지 않은 데이터는 유실 된다.

파일 저장방법  
- 자동 : redis.conf 파일에서 SAVE옵션 사용으로 시간별 저장 가능
- 수동 : BGSAVE 커맨드를 사용하여 cli창에서 수동으로 파일 저장 가능

#### AOF (Append Only File) 
: 명령어 저장  
- 데이터 변경(write)명령어가 들어오면 그대로 저장한다.  
- RDB 방식에 비해 내구성이 좋다.  
- Redis 프로토콜 형태로 저장.  
- AOF 로그는 추가 전용 로그 이므로 정전이 발생해도 찾거나 손상되는 문제가 없다.  
- RDB 방식 비해 데이터 용량이 커서 주기적으로 압축해 줘야 한다.

파일 저장방법  
- 자동 : redis.conf 파일에서 auto-aof-rewrite-percentage 옵션 사용으로 용량 크기 별 저장 가능  
- 수동 : BGREWRITERAOF 커멘드를 사용하여 cli창에서 수동으로 저장 가능

#### No persistence
- 지속성을 비활성화 해서 캐싱 할 때 사용한다.

#### RDB + AOF 
- 두 가지 방식을 조합하여 사용하면 PostgreSQL수준의  안정성을 제공한다.

#### 어떤 저장 방식을 사용 할건지
- 어느정도의 데이터 손실이 발생해도 괜찮은 경우 RDB 단독 사용
- 장애 직전 데이터가 전부 보장(최대 1초 유실 가능)이 되어야 할 경우 AOF 사용.  
	- 공식 사이트에선 데이터베이스 백업, 빠른 재시작, AOF 엔진의 버그 발생 시를 대비해 때때로 RDB 스냅샷을 생성하는 것이 좋으므로 단독 사용은 권장하지 않는다고 함.

## Redis 트랜잭션
- 한 번에 여러 명령을 묶어서 실행 하는 것. (MULTI, EXEC, DISCARD, WATCH등) 
- 모든 명령은 직렬화 되어 순차적으로 실행 되며 하나의 격리된 작업으로 실행 된다.(독립성 보장)
- Batch 단위로 실행된다.  

#### 커맨드
- MULTI
    - Redis의 트랜잭션을 시작하는 커맨드. 트랜잭션을 시작하면 Redis는 이후 커맨드는 바로 실행되지 않고 Queue에 쌓인다.
- EXEC
    - 정상적으로 처리되어 Queue에 쌓여있는 명령어를 일괄적으로 실행한다. (Commit과 동일)
- DISCARD
    - queue에 쌓여있는 명령어를 실괄적으로 폐기한다. (Rollback과 동일)
- WATCH
    - Redis에서 Lock 명령어. 낙관적 락(Optimistic Lock) 기반.
    - **WATCH 명령어를 사용하면 이 후 UNWATCH 되기전에는 1번의 EXEC 또는 Transaction 아닌 다른 커맨드만 허용**한다.
    - 동시성 문제에서 단순히 MULTI, EXEC 만으로 트랜잭션의 Isolation을 보장할 수 없기 때문에 사용되며, WATCH로 인하여 예외가 발생했을 때 트랜잭션의 Queue에 쌓여있는 커맨드들을 폐기하는 DISCARD 명령어 등을 통해 처리할 수 있다.  

#### 장점
- 동시성 처리 가능(WATCH와 조합)  

#### 단점
- 트랜잭션 내에서 실패하면 단순성과 성능에 큰 영향을 미치기 때문에 전체 롤 백이 되지 않고 성공한 커맨드는 실행된다. (원자성 보장 X)
- ACID를 만족시키지 않는다.
- Redis 트랜잭션으로 수행할 수 있는 모든 작업은 스크립트로도 수행할 수 있으며, 일반적으로 스크립트가 더 간단하고 빠르다. 

## Redis의 자료구조
#### Strings
- 가장 기본적인 Redis 데이터 유형으로, 바이트 시퀀스를 나타낸다.   
  
#### Bitmaps
- String의 변형으로 비트 단위의 연산이 가능  

#### Lists
- 데이터를 순서대로 저장. 큐로 사용할 수 있음  

#### Sets
- 자바의 HashSet과 같은 중복되지 않은 데이터  

#### Hashes
- 필드-value값이 쌍으로 저장되는 데이터  

#### Sorted sets
- score - 사전 기준으로 정렬되어 저장되는 set    

#### Streams
- 로그를 저장하기 좋은 자료구조   

#### HyperLogLog
- 중복되지 않은 값을 세기 위해 사용 (데이터 크기가 큼)



[Transactions | Redis](https://redis.io/docs/interact/transactions/)

[Redis 동시성 처리를 위한 Transaction 사용 (MULTI, EXEC, DISCARD, WATCH) (tistory.com)](https://wildeveloperetrain.tistory.com/137)