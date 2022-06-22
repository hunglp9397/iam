package com.hunglp.iambackend.service.impl;

import com.hunglp.iambackend.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;


@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate<String, String> template;

    @Override
    public void saveKeyValue(String key, String value) {
        template.opsForValue().set(key, value);
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
    public String getValueByKeyPrefix(String keyPattern) {

        ScanOptions scanOptions = ScanOptions.scanOptions().match(keyPattern).count(20).build();
        Cursor c = template.getConnectionFactory().getConnection().scan(scanOptions); // scanning in db
        while (c.hasNext()) {
            System.out.println(c.next());
        }

        return "abc";


    }
}
