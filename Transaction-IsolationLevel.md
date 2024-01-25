# <트랜잭션 Isolation level>
## 격리 수준(Isolation level)
- 여러 트랜잭션이 동시에 처리될 때 특정 트랜잭션이 다른 트랜잭션에서 변경하거나 조회하는 데이터를 볼 수 있게 허용할지 말지를 결정하는 것
- 수행중인 트랜잭션에 다른 트랜잭션이 영향을 주면 안되며 독립적 작동해야 함
- SERIALIZABLE 격리 수준이 아니면 크게 성능에 상관없다.
- Locking 기반으로 고립화 수준을 조정

Isolation level에서는 아래의 세 가지 부정합 문제가 발생할 수 있다.
### 읽기 이상 현상(Read Phenomena)
#### 1. <span style="background:rgba(240, 107, 5, 0.2)">Dirty Read</span>
- 트랜잭션 처리 중인 작업의 중간 결과를 볼 수 있는 것.(커밋되지 않은 정보 읽기)

#### 2. <span style="background:rgba(240, 107, 5, 0.2)">Non-repeatable Read</span>
- 한 트랜잭션 내에서 같은 조회 쿼리를 두 번 실행 했을 때, 그 두 개의 값이 다른 현상.(다른 트랜잭션에서 수정하여 커밋한 결과)  

#### 3. <span style="background:rgba(240, 107, 5, 0.2)">Phantom Read</span>
- 한 트랜잭션 내에서 첫번째 쿼리 결과와 두번째 쿼리의 결과가 다른 현상. (row가 새로 생기거나 없어짐)

## Isolation level 특징
### Read Uncommitted
- 다른 트랜잭션에서 COMMIT이나 ROLLBACK 되지 않은 데이터들을 읽을 수 있다. ROLLBACK이 되면 데이터베이스에 없는 데이터를 읽어 온다.

	**발생하는 읽기 이상 현상**  
	1.  dirty read  
	2.  non-repeatable read  
	3.  phantom read

### Read committed
- 다른 트랜잭션에서 데이터를 변경하고 COMMIT이 완료된 데이터만 조회 가능.
- 오라클의 기본 isolation level.
- 온라인 서비스에서 가장 많이 선택.

	**발생하는 읽기 이상 현상**  
	1.  non-repeatable read  
	2.  phantom read

### Repeatable Read
- 하나의 트랜잭션이 수정한 행을 다른 트랜잭션이 수정할 수 없도록 막아주지만, 새로운 행을 추가하는 것은 막지 않음.
- MySQL InnoDB의 기본 isolation level. 

	**발생하는 읽기 이상 현상**  
	1.  phantom read

### SERIALIZABLE
- 읽기 작업도 shared lock(or read lock) 획득해야 한다. 다른 트랜잭션들이 절대 접근 불가.
- 동시성 하락, 안정성 증가



[MySQL의 Transaction Isolation Levels (jupiny.com)](https://jupiny.com/2018/11/30/mysql-transaction-isolation-levels/)
