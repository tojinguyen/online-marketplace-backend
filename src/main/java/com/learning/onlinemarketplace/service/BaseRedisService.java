package com.learning.onlinemarketplace.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class BaseRedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public BaseRedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Lưu trữ một giá trị vào Redis với thời gian sống (TTL).
     *
     * @param key     Khóa
     * @param value   Giá trị
     * @param timeout Thời gian sống (TTL) tính bằng giây
     */
    public void set(String key, Object value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * Lưu trữ một giá trị vào Redis không có thời gian sống (TTL).
     *
     * @param key   Khóa
     * @param value Giá trị
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * Lấy giá trị từ Redis theo khóa.
     *
     * @param key Khóa
     * @return Giá trị tương ứng hoặc null nếu không tồn tại
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * Xóa một khóa khỏi Redis.
     *
     * @param key Khóa cần xóa
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * Kiểm tra xem một khóa có tồn tại trong Redis hay không.
     *
     * @param key Khóa cần kiểm tra
     * @return true nếu tồn tại, ngược lại false
     */
    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * Thiết lập thời gian sống (TTL) cho một khóa.
     *
     * @param key     Khóa
     * @param timeout Thời gian sống (TTL) tính bằng giây
     * @return true nếu thiết lập thành công, ngược lại false
     */
    public boolean expire(String key, long timeout) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, timeout, TimeUnit.SECONDS));
    }
}

