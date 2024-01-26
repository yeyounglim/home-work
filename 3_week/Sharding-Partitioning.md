# <Sharding 과 Partitioning>
## 파티셔닝(Partitioning)
- 큰 테이블이나 인덱스를 작은 파티션(Partition) 단위로 나누어 관리하는 기법(큰 테이블을 작은 테이블로 나누는 것)
- 데이터베이스에서 중요한 튜닝 기법으로, 데이터가 너무 커져서 조회하는 시간이 길어질 때 시행. 

#### 장점
##### 성능 (Performance)
- 특정 Query의 성능을 향상
- 대용량 Data Write 환경에서 효율적
- 필요한 데이터만 빠르게 조회할 수 있으므로 쿼리가 가벼워진다.
- Full Scan에서 데이터 접근의 범위를 줄여 성능 향상. 


##### 가용성 (Availability)
- 물리적인 파티셔닝으로 전체 데이터의 훼손 가능성 하락
- 파티션 별로 독립적으로 백업하고 복구 가능
- 테이블의 파티션 단위로 Disk I/O를 분산하여 경합을 줄이므로 UPDATE 성능 향상   

##### 관리 용이성 (Manageability)
- 큰 테이블들을 제거하여 관리가 쉬워진다.

#### 단점
- Join 비용이 증가
- 테이블과 인덱스를 별도로 파티셔닝 할 수 없기 때문에 인덱스도 파티셔닝 해야 함

#### 수직 파티셔닝(vertical partitioning)
- column 기준으로 테이블을 나누는 방식. 
- 정규화도 버티컬 파티셔닝
- select 할 때 join을 해도 메모리에는 테이블 전체가 올라온다.
	사이즈가 크고 사용하지 않는 컬럼도 함께 조회 되기 때문에 빠른 속도를 위하여 컬럼을 분리 할 수 있다. 
- 민감 정보를 제외 시키고 싶을 경우 사용
- 자주 사용되는 컬럼만 모으고 싶을 경우 사용
- 데이터를 찾는 과정이 기존보다 복잡하여 지연 속도(Latency*) 증가

\* 네트워크에서 하나의 데이터 패킷이 한 지점에서 다른 지점으로 보내지는 데 소요되는 시간.

#### 수평 파티셔닝(horizontal partitioning)
- row 기준으로 테이블을 나누는 방식. 
- 스키마는 그대로 유지 됨
- 테이블의 크기가 커질수록 인덱스의 크기도 커져서 느려짐
- hash function을 사용하여 테이블을 분리
- 데이터가 균일하게 분할 되도록 하는게 포인트
- 한 번 파티션이 나뉘면 이후에 파티션을 추가하기 까다로움
- 하나의 DB서버에 저장됨(트래픽 부하 가능성)
- 파티셔닝 기준이 되는 컬럼이 partition key가 된다.

## Sharding
- row 기준으로 테이블을 나누는 방식. 수평 파티셔닝이랑 같다.
- 독립된 DB서버에 저장 됨(트래픽 부하를 분산)
- partition key가 shard key가 됨
- 파티션을 shard 라고 한다.

## Replication
- DB를 복제해서 여러 DB서버에 저장하는 방식.
- 원본-복제본이 master-slave/primary-secondary로 불림
- 고 가용성(**H**igh **A**vailablity) : 원본이 장애가 생길 경우 백엔드 서버가 복제본으로 전환(failover) 시킴
- read쿼리 일부를 복제본으로 분산 하여 서버 부하를 낮춰 줄 수 있다.

 [https://code-lab1.tistory.com/202](https://code-lab1.tistory.com/202)  
 
 [https://code-lab1.tistory.com/202](https://code-lab1.tistory.com/202)
