package org.example.redisstudy.redisHash.service;

import lombok.RequiredArgsConstructor;
import org.example.redisstudy.redisHash.dto.RefreshToken;
import org.example.redisstudy.redisHash.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final RefreshTokenRepository tokenRepository;

    public void login(String userId, String token) {
        RefreshToken rt = new RefreshToken(userId, token, 604800L); // 604,800초는 7일. 즉 토큰의 유통기한은 7일.
        tokenRepository.save(rt);
    }
}
