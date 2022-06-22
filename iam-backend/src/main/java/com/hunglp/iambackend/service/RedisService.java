package com.hunglp.iambackend.service;


import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;

public interface RedisService {

    String getValueByKey(String key);

    void saveKeyValue(String key, String value);

    void deleteValueByKey(String key);

    void updateValueByKey(String key, String value);

    String getValueByKeyPrefix(String keyPattern);



}
