package com.memoforward.vod.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConstantVodUtils implements InitializingBean {

    @Value("${aliyun.vod.file.keyid}")
    String accessKeyId;

    @Value("${aliyun.vod.file.keysecret}")
    String accessKeySecret;

    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;

    @Override
    public void afterPropertiesSet() throws Exception {
        ACCESS_KEY_ID = this.accessKeyId;
        ACCESS_KEY_SECRET = this.accessKeySecret;
    }
}
