package com.hunglp.iambackend.service;


import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;

import java.util.List;

public interface RedisService {

    String getValueByKey(String key);

    void saveKeyValue(String key, String value);

    void deleteValueByKey(String key);

    void updateValueByKey(String key, String value);

    List<String> getKeyPrefix(String keyPattern);

}
