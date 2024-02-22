# 사용 할 API 정리


### 1. 서울시 버스노선별 정류장별 시간대별 승하차 인원 정보
![](https://i.imgur.com/LRAD2TY.png)

- http://data.seoul.go.kr/dataList/OA-12913/S/1/datasetView.do
- 매월 5일 갱신
- API 있음

### 2. 서울시 버스정류소 위치정보 ARS(안쓸수도. 6번이 대체)
![](https://i.imgur.com/xN42f8X.png)
- api 있음

### 3. 버스 승강장 혼잡 정보 서비스
![](https://i.imgur.com/S0rqoSr.png)
- api 있음

### 4. 서울특별시_정류소정보조회 서비스
![](https://i.imgur.com/xOYQ233.png)
![](https://i.imgur.com/J7A4J3X.png)

- api 있음
- https://www.data.go.kr/data/15000303/openapi.do

### 5. 서울특별시_버스도착정보조회 서비스

![](https://i.imgur.com/lSrGbhq.png)
![](https://i.imgur.com/A2S1Uau.png)
![](https://i.imgur.com/WHsqHZD.png)
![](https://i.imgur.com/uUJHNvD.png)
- https://www.data.go.kr/data/15000314/openapi.do
- api 있음
- 혼잡도 부분만 사용하기
### 6. 서울시 버스 노선 정보 조회
![](https://i.imgur.com/zyz6uzG.png)

- http://data.seoul.go.kr/dataList/OA-1095/F/1/datasetView.do
- 그냥 csv 파일 데이터
- 업데이트 주기 랜덤

### 7. 정류장 정보
![](https://i.imgur.com/RiCAVru.png)

- 둘다 있는데 api조건이 페이지조회 조건뿐이고 모르는 정보가 많아서 파일데이터가 나음 
- 분기별 1회 업데이트
- [T Data 서울교통빅데이터플랫폼 > 파일데이터 상세정보 (seoul.go.kr)](https://t-data.seoul.go.kr/dataprovide/trafficdataviewfile.do?data_id=64)




### 알아 낸 것
- node_id랑 버스 정류소 고유 id랑 같다?(데이터 보니 같은데 교통정보시스템 교차로(노드) 정보라고 함)
- 링크 아이디는 비어있을수 있음
- ars_id가 서울 버스 정류소 아이디 이다(정류소에 써져있음)
- api마다 id를 부르는 이름이 다르다 
- 결국 ars_id랑 고유id 등등 조합해서 쓴다