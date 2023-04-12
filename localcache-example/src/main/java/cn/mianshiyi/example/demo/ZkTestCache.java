package cn.mianshiyi.example.demo;


import cn.mianshiyi.localcache.client.zk.ZkAbstractLocalCache;
import cn.mianshiyi.localcache.client.zk.ZkCacheConfig;

import javax.annotation.PostConstruct;
import java.util.UUID;

/**
 * @author shangqing.liu
 */
//@Service
public class ZkTestCache extends ZkAbstractLocalCache<String, String> {

    @PostConstruct
    public void init() {
        ZkCacheConfig config = new ZkCacheConfig(10, 100, 60 * 60
                , "/test1/zkPath", "127.0.0.1:2181");
        super.init(config);
    }

    @Override
    public String refresh(String key) {
        return UUID.randomUUID().toString();
    }

    @Override
    public void receive(byte[] data) {
        String s = String.valueOf(data);
        setCache(s, UUID.randomUUID().toString());
    }

}
