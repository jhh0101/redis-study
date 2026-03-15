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

***
# 🗺️ Spring Data Redis Repository (@RedisHash) 정복 로드맵
### 설정과 준비 (Configuration)
JPA처럼 레디스를 쓰기 위한 기본 설정과 어노테이션 활성화 방법을 배웁니다.         
학습 내용: @EnableRedisRepositories 설정, 기본 객체(Entity) 매핑 개념.            
실무 예시: 스프링 부트 프로젝트에 레디스 리포지토리 기능 켜기.                

### Chapter 1. 기본 CRUD와 수명 관리 (@RedisHash & @TimeToLive)
복잡한 직렬화나 레디스 명령어 없이, 자바 객체를 통째로 넣고 빼는 마법입니다.            
학습 내용: @RedisHash, @Id, @TimeToLive를 활용한 엔티티 설계 및 CrudRepository 상속.                
실무 예시: 로그인 시 발급된 리프레시 토큰(Refresh Token)을 14일 뒤 자동 삭제되도록 레디스에 저장하고 꺼내 쓰기.            

### Chapter 2. 보조 인덱스 검색 (@Indexed)
Key-Value 저장소의 한계를 넘어, 내가 원하는 특정 필드값으로 데이터를 검색합니다.          
학습 내용: @Indexed 어노테이션 적용, 보조 키(Secondary Index)가 레디스에 생성되는 원리 이해.           
실무 예시: PK(유저 ID)가 아닌, 클라이언트가 HTTP 헤더로 보낸 '토큰 문자열' 자체로 레디스에서 유저 정보 찾아오기.         

### Chapter 3. 만료 이벤트와 유령 키 (Keyspace Notifications & Phantom Key)
스프링이 백그라운드에서 데이터를 지우고 이벤트를 발생시키는 '흑마법'의 실체를 파헤칩니다.          
학습 내용: 데이터 만료 시 발생하는 이벤트 가로채기, 섀도우 키(Phantom Key)로 인한 메모리 오버헤드 원리 파악.           
실무 예시: 레디스에서 임시 장바구니 데이터가 TTL 만료로 삭제되는 순간을 감지해서, RDB(MySQL)에 '장바구니 만료 로그' 남기기.          

### Chapter 4. 실무 아키텍처 적용 (Trade-off & Best Practice)           
모든 기술에는 장단점이 있습니다. 상황에 맞춰 최적의 레디스 무기를 선택하는 안목을 기릅니다.                        
학습 내용: @RedisHash의 편의성과 메모리 낭비 사이의 트레이드오프 계산, RedisTemplate과의 용도 분리.            
실무 예시: 데이터 구조가 복잡한 이메일 인증 코드는 @RedisHash로 처리하고, 극한의 메모리 최적화가 필요한 조회수 카운터는 RedisTemplate으로 분리해서 아키텍처 설계하기.               

# 🗺️ Redis 고가용성(HA) 및 아키텍처 정복 로드맵
### Chapter 1. 기본 복제 구조 (Replication / Master-Slave)
데이터를 잃지 않기 위해 레디스를 여러 대 두는 가장 기초적인 방법입니다.   
학습 내용: 마스터(Master) 노드와 레플리카(Replica/Slave) 노드의 데이터 동기화 원리.          
실무 예시: 쓰기(Write) 작업은 마스터로, 읽기(Read) 작업은 레플리카로 분산시켜 트래픽 분산하기.            

### Chapter 2. 자동 장애 복구 (Redis Sentinel)
마스터 서버가 다운되었을 때 관리자가 새벽에 깨지 않아도 되는 '자동화' 시스템입니다.            
학습 내용: 센티널(초소병) 노드의 감시 로직, 쿼럼(Quorum) 과반수 투표를 통한 새로운 마스터 승진(Failover) 원리.           
실무 예시: 메인 레디스 서버가 뻗었을 때, 서비스 중단 없이 3초 만에 예비 서버로 자동 전환하기.            

### Chapter 3. 대용량 분산 처리 (Redis Cluster)
데이터가 너무 많아서 서버 한 대의 메모리로는 감당이 안 될 때 데이터를 쪼개는 기술입니다.         
학습 내용: 데이터 샤딩(Sharding), 해시 슬롯(Hash Slot) 알고리즘, 클러스터 노드 간의 통신(Gossip Protocol).         
실무 예시: 경매 시스템의 트래픽이 폭주하여 데이터가 100GB를 넘어갈 때, 여러 대의 서버에 데이터를 균등하게 나누어 저장하기.           

### Chapter 4. 캐시 아키텍처 패턴 (Cache Strategies)
단순히 레디스 서버를 띄우는 것을 넘어, DB와 레디스 사이의 데이터를 어떻게 동기화할지 설계합니다.      
학습 내용: Look-Aside(지연 로딩), Write-Through(동시 쓰기), Write-Behind(모아서 쓰기) 패턴의 장단점.           
실무 예시: 경매 입찰처럼 쓰기가 아주 빈번한 로직은 Write-Behind를 적용하고, 상품 상세 조회는 Look-Aside로 설계하여 DB 부하 최소화하기.           