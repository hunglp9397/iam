package com.hunglp.iambackend.service;


public interface RedisService {

    String getValueByKey(String key);

    void saveKeyValue(String key, String value);

    void deleteValueByKey(String key);

    void updateValueByKey(String key, String value);
}
