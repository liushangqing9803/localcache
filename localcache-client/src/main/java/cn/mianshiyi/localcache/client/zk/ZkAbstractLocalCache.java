package cn.mianshiyi.localcache.client.zk;


import cn.mianshiyi.localcache.client.AbstractLocalCache;
import cn.mianshiyi.localcache.client.Broadcast;
import cn.mianshiyi.localcache.client.util.ZookeeperUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.TimeUnit;

/**
 * 使用zookeeper作为广播渠道
 * 来更新各client中的缓存
 * 默认采用caffeine cache 作为缓存使用
 * 实现类需要 CacheConfig 配置类中需要传入zkAddr(集群使用 ip1:port1,ip2:port2)以及监听路径zkCachePath
 * @author shangqing.liu
 */
public abstract class ZkAbstractLocalCache<K, V> implements AbstractLocalCache<K, V>, Broadcast {

    private ZkCacheConfig cacheConfig;

    private Cache<K, V> cache;

    private CuratorFramework curatorFramework;

    public void init(ZkCacheConfig zkCacheConfig) {
        this.cacheConfig = zkCacheConfig;
        if (cacheConfig.getZkAddr() == null || "".equals(cacheConfig.getZkAddr())) {
            throw new RuntimeException("localcache zk addr must not null");
        }
        cache = Caffeine.newBuilder()
                //cache的初始容量
                .initialCapacity(cacheConfig.getInitialCapacity())
                //cache最大缓存数
                .maximumSize(cacheConfig.getMaximumSize())
                //设置写缓存后n秒钟过期
                .expireAfterWrite(cacheConfig.getExpireAfterWrite(), TimeUnit.SECONDS)
                .build();
        try {
            curatorFramework =
                    CuratorFrameworkFactory.builder().connectString(cacheConfig.getZkAddr()).sessionTimeoutMs(4000).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
            curatorFramework.start();
            ZookeeperUtil.createPersistent(curatorFramework, cacheConfig.getZkCachePath());
            NodeCache nodeCache = new NodeCache(curatorFramework, cacheConfig.getZkCachePath(), false);
            NodeCacheListener nodeCacheListener = () -> {
                ChildData currentData = nodeCache.getCurrentData();
                if (currentData.getData() == null) {
                    return;
                }
                this.receive(currentData.getData());
            };
            nodeCache.getListenable().addListener(nodeCacheListener);
            nodeCache.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public final V getCache(K key) {
        return cache.get(key, k -> refresh(key));
    }

    @Override
    public final V setCache(K key, V value) {
        cache.put(key, value);
        return value;
    }

    @Override
    public final void broadcast(byte[] data) {
        try {
            ZookeeperUtil.setData(curatorFramework, this.cacheConfig.getZkCachePath(), data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
