package cn.mianshiyi.example.demo;


import cn.mianshiyi.localcache.client.jgroup.JGroupAbstractLocalCache;
import cn.mianshiyi.localcache.client.jgroup.JGroupCacheConfig;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.UUID;

/**
 * @author shangqing.liu
 */
@Service
public class JgroupTestCache extends JGroupAbstractLocalCache<String, String> {
    @PostConstruct
    public void init() {
        JGroupCacheConfig config = new JGroupCacheConfig(10, 100, 60 * 60, "jName");
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
