# 🗺️ RedisTemplate 정복 로드맵
### Chapter 1. String 자료구조 (opsForValue)
가장 기본이자 전체 사용량의 70% 이상을 차지하는 핵심입니다.         
학습 내용: 단순 키-값 저장, 조회, 삭제, 그리고 자동 삭제(TTL).    
실무 예시: 이메일 인증번호, 로그인 세션, 단순 캐싱.

### Chapter 2. List 자료구조 (opsForList)
순서가 있는 데이터의 묶음입니다.      
학습 내용: 양방향 넣기(Push), 꺼내기(Pop), 범위 조회.       
실무 예시: 최근 본 상품 목록, 간단한 메시지 큐.

### Chapter 3. Set 자료구조 (opsForSet)
중복을 허용하지 않는 주머니입니다.     
학습 내용: 데이터 추가, 중복 체크, 집합 연산(교집합/합집합).   
실무 예시: 상품에 태그 등록, 상품에 달린 태그 전부 조회, 상품에 특정 태그 존재 여부 확인

### Chapter 4. Sorted Set 자료구조 (opsForZSet)
순서(가중치)가 있는 중복 없는 세트입니다. (레디스의 꽃)   
학습 내용: 스코어(Score) 기반 정렬, 랭킹 조회.     
실무 예시: 실시간 인기 검색어, 게임 점수 랭킹, 선착순 대기열.

### Chapter 5. Hash 자료구조 (opsForHash)
키 하나 안에 또 다른 키-값들이 들어있는 구조입니다.  
학습 내용: 필드 단위 수정, 특정 필드만 가져오기.   
실무 예시: 유저 프로필 정보(이름, 나이, 등급 등을 하나의 키로 관리).

***

# 🗺️ Redisson & 분산 시스템 정복 로드맵
### Redisson 설정과 연결 (Configuration)
기존의 Lettuce(RedisTemplate)와는 다른 Redisson만의 연결 방식을 배웁니다.     
학습 내용: RedissonClient 빈 등록, Single/Cluster 서버 설정.       
실무 예시: 내 프로젝트에 Redisson 엔진 장착하기.

### Chapter 1. 분산 락의 기초 (Simple RLock)
서버가 여러 대여도 단 한 명만 진입을 허용하는 '자물쇠'의 기본입니다.        
학습 내용: lock(), unlock(), 그리고 락 획득 대기 시간(waitTime) 설정.       
실무 예시: [경매 입찰] - 0.01초 차이로 들어온 중복 입찰 방어하기.

### Chapter 2. 어노테이션 분산 락 (AOP & Annotation)
비즈니스 로직과 락 로직을 분리하여 코드를 깔끔하게 만듭니다. (취업 치트키)         
학습 내용: 커스텀 어노테이션(@DistributedLock) 제작, AOP를 통한 락 적용.        
실무 예시: 복잡한 서비스 로직에 어노테이션 한 줄로 동시성 제어하기.

### Chapter 3. 고급 분산 객체 (Distributed Objects)
레디스 자료구조를 자바의 컬렉션(Map, List)처럼 아주 편하게 쓰는 법입니다.      
학습 내용: RMap, RList, RTopic(Pub/Sub) 활용법.        
실무 예시: 여러 서버 간의 실시간 알림 시스템, 분산 캐시 동기화.

### Chapter 4. 트래픽 제어와 안전장치 (Advanced Patterns)
단순한 락을 넘어 서버를 보호하는 고급 기술들을 배웁니다.        
학습 내용: RRateLimiter(요청 제한), RSemaphore(동시 접속자 제한).      
실무 예시: 특정 유저의 악의적인 연타 공격 방어, 선착순 100명 입장 제한.
