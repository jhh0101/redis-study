package org.example.redisstudy.redisHash.repository;

import org.example.redisstudy.redisHash.dto.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    Optional<RefreshToken> findByToken(String token);   // PK(userId)가 아닌 값으로 데이터를 찾고 싶을 때 사용

    // JPA와 유사함
}
