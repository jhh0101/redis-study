# 1. 자바 21 버전 사용
FROM eclipse-temurin:21-jdk-alpine

# 2. 빌드된 jar 파일을 컨테이너 안으로 복사
# (미리 ./gradlew build를 실행해서 jar 파일을 만들어둬야 합니다)
COPY build/libs/*.jar app.jar

# 3. 앱 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]