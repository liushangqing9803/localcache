package cn.mianshiyi.example.demo;

import cn.mianshiyi.localcache.client.CacheConfig;
import cn.mianshiyi.localcache.client.ZkAbstractLocalCache;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author shangqing.liu
 */
@Service
public class ZkTestCache extends ZkAbstractLocalCache<Long, String> {
    @Override
    public String refresh(String key) {
        String s = UUID.randomUUID().toString();
        System.out.println("key" + key);
        System.out.println("刷新" + getCacheConfig().getZkCachePath() + "--" + s);
        String s1 = setCache(Long.valueOf(key), s);
        System.out.println(s1);
        return s;
    }

    @Override
    public CacheConfig getCacheConfig() {
        CacheConfig cacheConfig = new CacheConfig(10, 100, 1000);
        cacheConfig.setZkCachePath("/localcache/0307test001");
        cacheConfig.setZkAddr("127.0.0.1:2181");
        return cacheConfig;
    }

}
