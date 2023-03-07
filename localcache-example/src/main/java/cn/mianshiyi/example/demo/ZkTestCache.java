package cn.mianshiyi.example.demo;

import cn.mianshiyi.localcache.client.ZkAbstractLocalCache;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author shangqing.liu
 */
@Service
public class ZkTestCache extends ZkAbstractLocalCache<Long, String> {
    @Override
    public String refresh(String key) throws Exception {
        String s = UUID.randomUUID().toString();
        System.out.println("key" + key);
        System.out.println("刷新" + path() + "--" + s);
        String s1 = setCache(Long.valueOf(key), s);
        System.out.println(s1);
        return s;
    }

    @Override
    protected String path() {
        return "/localcache/0307test001";
    }

    @Override
    protected String zkAddr() {
        return "127.0.0.1:2181";
    }
}
