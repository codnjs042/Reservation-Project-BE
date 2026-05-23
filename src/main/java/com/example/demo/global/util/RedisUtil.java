package com.example.demo.global.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {
    private final RedisTemplate<String, Object> redisTemplate;

    public void set(String key, Object value, long ttl) {
        redisTemplate.opsForValue().set(key, value, ttl, TimeUnit.SECONDS);
    }

    public String get(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        return value == null ? null : value.toString();
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
