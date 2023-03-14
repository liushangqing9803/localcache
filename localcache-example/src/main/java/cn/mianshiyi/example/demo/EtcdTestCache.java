package cn.mianshiyi.example.demo;

import cn.mianshiyi.localcache.client.CacheConfig;
import cn.mianshiyi.localcache.client.EtcdAbstractLocalCache;
import cn.mianshiyi.localcache.client.ZkAbstractLocalCache;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author shangqing.liu
 */
@Service
public class EtcdTestCache extends EtcdAbstractLocalCache<String> {
    @Override
    public String refresh(String key) {
        String s = UUID.randomUUID().toString();
        System.out.println("key" + key);
        System.out.println("刷新" + getCacheConfig().getEtcdCachePath() + "--" + s);
        return s;
    }

    @Override
    public CacheConfig getCacheConfig() {
        CacheConfig cacheConfig = new CacheConfig(10, 100, 1000);
        cacheConfig.setEtcdCachePath("/localcacheetcd/0307test001");
        String[] add = new String[]{"http://127.0.141.80:2379"};
        cacheConfig.setEtcdAddr(add);
        return cacheConfig;
    }

}
