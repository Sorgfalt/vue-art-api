package com.vueart.api.common.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public void saveRefreshToken(Long userId, String refreshToken) {
        redisTemplate.opsForValue().set("refreshToken: " + userId, refreshToken, 7, TimeUnit.DAYS );
    }

    public String getRefreshToken(Long userId) {
        return (String) redisTemplate.opsForValue().get("refreshToken: " + userId);
    }
}
