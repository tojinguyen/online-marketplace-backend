package com.learning.onlinemarketplace.redisservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class BaseRedisService {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public BaseRedisService(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Lưu một Object vào Redis dưới dạng JSON với TTL.
     */
    public <T> void set(String key, T value, long timeout) {
        try {
            String jsonValue = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, jsonValue, timeout, TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            log.error("Lỗi khi serializing object: {}", e.getMessage());
        }
    }

    /**
     * Lưu một Object vào Redis không có TTL.
     */
    public <T> void set(String key, T value) {
        try {
            String jsonValue = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, jsonValue);
        } catch (JsonProcessingException e) {
            log.error("Lỗi khi serializing object: {}", e.getMessage());
        }
    }

    /**
     * Lấy dữ liệu từ Redis và chuyển thành Object.
     */
    public <T> T get(String key, Class<T> clazz) {
        try {
            String jsonValue = redisTemplate.opsForValue().get(key);
            return jsonValue != null ? objectMapper.readValue(jsonValue, clazz) : null;
        } catch (Exception e) {
            log.error("Lỗi khi deserializing object: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Xóa một khóa khỏi Redis.
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * Kiểm tra một khóa có tồn tại hay không.
     */
    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * Thiết lập thời gian sống (TTL) cho một khóa.
     */
    public boolean expire(String key, long timeout) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, timeout, TimeUnit.SECONDS));
    }
}
