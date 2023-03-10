package cn.mianshiyi.localcache.client;

import cn.mianshiyi.localcache.client.utils.ZookeeperUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @author shangqing.liu
 */
public abstract class ZkAbstractLocalCache<K, V> implements AbstractLocalCache<K, V>, Broadcast {

    private Cache<K, V> CACHE;

    private CuratorFramework curatorFramework;

    @PostConstruct
    public final void init() throws Exception {
        CacheConfig cacheConfig = getCacheConfig();
        CACHE = Caffeine.newBuilder()
                //cache的初始容量
                .initialCapacity(cacheConfig.getInitialCapacity())
                //cache最大缓存数
                .maximumSize(cacheConfig.getMaximumSize())
                //设置写缓存后n秒钟过期
                .expireAfterWrite(cacheConfig.getExpireAfterWrite(), TimeUnit.SECONDS)
                .build();

        if (curatorFramework == null) {
            curatorFramework = CuratorFrameworkFactory.builder().connectString(cacheConfig.getZkAddr()).sessionTimeoutMs(4000).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
            curatorFramework.start();
        }
        ZookeeperUtil.createPersistent(curatorFramework, cacheConfig.getZkCachePath());
        NodeCache nodeCache = new NodeCache(curatorFramework, cacheConfig.getZkCachePath(), false);
        NodeCacheListener nodeCacheListener = () -> {
            ChildData currentData = nodeCache.getCurrentData();
            String newData = new String(currentData.getData());
            this.refresh(newData);
        };
        nodeCache.getListenable().addListener(nodeCacheListener);
        nodeCache.start();
    }

    @Override
    public final V getCache(K key) {
        return CACHE.get(key, k -> refresh(key.toString()));
    }


    @Override
    public final V setCache(K key, V value) {
        CACHE.put(key, value);
        return value;
    }

    @Override
    public final void broadcast(String key) {
        try {
            ZookeeperUtil.setData(curatorFramework, getCacheConfig().getZkCachePath(), key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
