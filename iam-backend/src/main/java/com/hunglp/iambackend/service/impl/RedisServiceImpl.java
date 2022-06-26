package com.hunglp.iambackend.service.impl;

import com.hunglp.iambackend.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate<String, String> template;

    @Override
    public void saveKeyValue(String key, String value) {
        template.opsForValue().set(key, value);
    }

    @Override
    public void saveKeyValueWithExpire(String key, String value, int seconds) {
        template.opsForValue().set(key, value);
        template.expire(key,seconds, TimeUnit.SECONDS);
    }

    @Override
    public void deleteValueByKey(String key) {
        template.delete(key);
    }

    @Override
    public void updateValueByKey(String key, String value) {
        template.opsForValue().set(key, value);
    }

    @Override
    public String getValueByKey(String key) {
        return template.opsForValue().get(key);
    }

    @Override
    public List<String> getKeyPrefix(String keyPattern) {
        List<String> listKeys = new ArrayList<>();
        RedisConnection redisConnection = null;

        redisConnection = template.getConnectionFactory().getConnection();
        ScanOptions options = ScanOptions.scanOptions().match(keyPattern).count(100).build();

        Cursor c = redisConnection.scan(options);
        while (c.hasNext()) {
            listKeys.add(new String((byte[]) c.next()));
        }

        return listKeys;
    }

}
