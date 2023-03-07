package cn.mianshiyi.localcache.client;

import cn.mianshiyi.localcache.client.exception.LocalCacheException;
import cn.mianshiyi.localcache.client.utils.ZookeeperUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author shangqing.liu
 */
public abstract class ZkAbstractLocalCache<K, V> implements AbstractLocalCache<K, V> {

    private Map<K, V> CACHE = new ConcurrentHashMap<>();

    private volatile CuratorFramework curatorFramework;

    protected abstract String path();

    protected abstract String zkAddr();

    @PostConstruct
    public final void init() throws Exception {
        if (curatorFramework == null) {
            synchronized (this) {
                if (curatorFramework == null) {
                    curatorFramework = CuratorFrameworkFactory.builder().connectString(zkAddr()).sessionTimeoutMs(4000).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
                    curatorFramework.start();
                }
            }
        }
        ZookeeperUtil.createPersistent(curatorFramework, path());
        NodeCache nodeCache = new NodeCache(curatorFramework, path(), false);
        NodeCacheListener nodeCacheListener = () -> {
            ChildData currentData = nodeCache.getCurrentData();
            String newData = new String(currentData.getData());
            this.refresh(newData);
        };
        nodeCache.getListenable().addListener(nodeCacheListener);
        nodeCache.start();
    }

    @Override
    public final Map<K, V> cache() {
        return CACHE;
    }

    @Override
    public final String getType() {
        return "zookeeper";
    }

    @Override
    public final V getCache(K key) {
        return CACHE.get(key);
    }

    @Override
    public final void broadcast(K key) throws Exception {
        this.broadcastClient(key);
    }

    @Override
    public final V setCache(K key, V value) {
        CACHE.put(key, value);
        return value;
    }

    @Override
    public final void broadcastClient(K key) throws Exception {
        ZookeeperUtil.setData(curatorFramework, path(), key.toString());
    }
}
