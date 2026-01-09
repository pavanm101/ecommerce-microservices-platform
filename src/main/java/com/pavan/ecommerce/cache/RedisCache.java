package com.pavan.ecommerce.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RedisCache implements Cache {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public RedisCache(ObjectMapper objectMapper, RedisTemplate<String, String> redisTemplate) {
        this.objectMapper = objectMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Object getItem(String key, Class type) {
        try {
            String jsonObject = redisTemplate.opsForValue().get(key);
            if (jsonObject == null)
                return null;
            return objectMapper.readValue(jsonObject, type);
        } catch (Exception e) {
            System.out.println("Redis getItem error: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Object setItem(String key, Object item) {
        try {
            String jsonItem = objectMapper.writeValueAsString(item);
            redisTemplate.opsForValue().set(key, jsonItem);
            return item;
        } catch (Exception e) {
            System.out.println("Redis setItem error: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void removeItem(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public Collection<Object> getList(String key, Class type) {
        return getListFromRedis(key, type);
    }

    @Override
    public Collection<Object> addItemToList(String key, Object item) {
        try {
            String jsonItem = objectMapper.writeValueAsString(item);
            redisTemplate.opsForSet().add(key, jsonItem);
            return this.getListFromRedis(key, item.getClass());
        } catch (Exception e) {
            System.out.println("Redis addItemToList error: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Collection<Object> removeItemFromList(String key, Object item) {
        try {
            String jsonItem = objectMapper.writeValueAsString(item);
            redisTemplate.opsForSet().remove(key, jsonItem);
        } catch (Exception e) {
            System.out.println("Redis removeItemFromList error: " + e.getMessage());
        }
        return getListFromRedis(key, item.getClass());
    }

    private Collection<Object> getListFromRedis(String key, Class type) {
        Collection<Object> list = new ArrayList<>();
        Set<String> members = redisTemplate.opsForSet().members(key);
        if (members != null) {
            members.forEach(jsonItem -> {
                try {
                    list.add(objectMapper.readValue(jsonItem, type));
                } catch (Exception e) {
                    System.out.println("Redis parse error: " + e.getMessage());
                }
            });
        }
        return list;
    }
}
