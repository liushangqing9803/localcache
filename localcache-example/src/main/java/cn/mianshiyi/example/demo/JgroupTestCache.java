package cn.mianshiyi.example.demo;

import cn.mianshiyi.localcache.client.CacheConfig;
import cn.mianshiyi.localcache.client.EtcdAbstractLocalCache;
import cn.mianshiyi.localcache.client.JGroupAbstractLocalCache;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author shangqing.liu
 */
@Service
public class JgroupTestCache extends JGroupAbstractLocalCache<String> {
    @Override
    public String refresh(String key) {
        String s = UUID.randomUUID().toString();
        return s;
    }

    @Override
    public CacheConfig getCacheConfig() {
        CacheConfig cacheConfig = new CacheConfig(10, 100, 1000);
        cacheConfig.setJgroupGroupName("wodehhh");
        return cacheConfig;
    }

}
