package cn.mianshiyi.localcache.client;

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
 * 使用zookeeper作为广播渠道
 * 来更新各client中的缓存
 * 默认采用caffeine cache 作为缓存使用
 * 实现类需要 CacheConfig 配置类中需要传入zkAddr(集群使用 ip1:port1,ip2:port2)以及监听路径zkCachePath
 * 实现类项目依赖spring，需要注入到spring中
 *
 * @author shangqing.liu
 */
public abstract class ZkAbstractLocalCache<V> implements AbstractLocalCache<V>, Broadcast {

    private Cache<String, V> CACHE;

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
            if (currentData.getData() == null) {
                return;
            }
            String cacheKey = new String(currentData.getData());
            V newCacheValue = this.refresh(cacheKey);
            this.setCache(cacheKey, newCacheValue);
        };
        nodeCache.getListenable().addListener(nodeCacheListener);
        nodeCache.start();
    }

    @Override
    public final V getCache(String key) {
        return CACHE.get(key, k -> refresh(key));
    }


    @Override
    public final V setCache(String key, V value) {
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
