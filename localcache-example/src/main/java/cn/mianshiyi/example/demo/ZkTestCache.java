package cn.mianshiyi.example.demo;

import cn.mianshiyi.localcache.client.CacheConfig;
import cn.mianshiyi.localcache.client.ZkAbstractLocalCache;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author shangqing.liu
 */
@Service
public class ZkTestCache extends ZkAbstractLocalCache<String> {
    @Override
    public String refresh(String key) {
        String s = UUID.randomUUID().toString();
        return s;
    }

    @Override
    public CacheConfig getCacheConfig() {
        CacheConfig cacheConfig = new CacheConfig(10, 100, 1000);
        cacheConfig.setZkCachePath("/lo333ache/0307test2222001");
        cacheConfig.setZkAddr("127.0.0.1:2181");
        return cacheConfig;
    }

}
