# 토비의 스프링(2021-01-29~)

- AOP에서 막혀서 다시 시작

## 시작하기
- 학습하는 장에서 작업을 시작
- 한 장이 끝난 경우
마스터에서 끝난 장을 머지하고 새로운 장의 브렌치를 만든다

## 목차
[1장 초난감DAO](#1장-초난감DAO)

## 1장 초난감DAO
- 공부 기간(2021/1/29 ~ 2021/1/30)
- 3번째 읽는 중^^..

### 결합도가 강한 초난감DAO
- 기본적인 DB서비스 과정
1) DB연결 정보를 제공하여 DB연결
2) 쿼리 생성
3) 쿼리 수행 및 결과 반환
4) DB연결 반환

### 매번 달라지는 DB연결 정보

1. DB연결이 바뀔 때마다 모든 메서드를 수행해야할까?
-> DB연결 작업만 메서드로 분리하기

2. 결국 클래스의 변경이 일어나는 건 막지 못할까?
-> DB연결을 추상메서드로 만들고 상속받아 재구현하기

3. 상속이 강제되면 다른 유연함을 포기해야할텐데?
-> 다른 클래스에게 DB연결 책임을 위임
private final DataSource dataSource;
DataSource는 인터페이스이며
DataSource의 구현체를 통해 초기화해야함

### DataSource의 초기화는 누가? 언제?

1. DataSource의 초기화를 소유 클래스가 직접한다면?
결국 DataSource의 구현체가 바뀔 때마다 소유클래스 코드 변경

2. 생성자를 이용한 초기화
외부에서 구현체를 직접 정한다.
제어의 역전, 의존성 주입 사용

### 초기화 책임의 분리

많은 객체를 다루기 위해 아예 생성 전용 클래스를 만든다.
-> 스프링컨테이너의 등장배경

결국
1. 프로그래머가 스프링컨테이너 조립
2. 프로그램 실행
3. 스프링컨테이너가 생성할 모든 클래스에 적절한 의존성 주입

의존성이 변경될 때마다 스프링컨테이너만 조작하면 된다.
그 외 다른 코드는 건드릴 필요가 없다.

