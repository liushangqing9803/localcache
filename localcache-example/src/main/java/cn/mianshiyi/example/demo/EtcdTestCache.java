package cn.mianshiyi.example.demo;


import cn.mianshiyi.localcache.client.etcd.EtcdAbstractLocalCache;
import cn.mianshiyi.localcache.client.etcd.EtcdCacheConfig;

import javax.annotation.PostConstruct;
import java.util.UUID;

/**
 * @author shangqing.liu
 */
//@Service
public class EtcdTestCache extends EtcdAbstractLocalCache<String, String> {

    @PostConstruct
    public void init() {
        String[] addr = new String[]{"http://127.0.0.1:2379"};
        EtcdCacheConfig config = new EtcdCacheConfig(10, 100, 60 * 60
                , "/test1/etcdPath", addr);
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
