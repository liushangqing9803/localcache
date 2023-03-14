package cn.mianshiyi.localcache.client;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.KV;
import io.etcd.jetcd.Watch;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author shangqing.liu
 */
public abstract class EtcdAbstractLocalCache<V> implements AbstractLocalCache<V>, Broadcast {

    private Cache<String, V> CACHE;

    private Client etcdClient;

    @PostConstruct
    public final void init() {
        CacheConfig cacheConfig = getCacheConfig();
        CACHE = Caffeine.newBuilder()
                //cache的初始容量
                .initialCapacity(cacheConfig.getInitialCapacity())
                //cache最大缓存数
                .maximumSize(cacheConfig.getMaximumSize())
                //设置写缓存后n秒钟过期
                .expireAfterWrite(cacheConfig.getExpireAfterWrite(), TimeUnit.SECONDS)
                .build();

        if (etcdClient == null) {
            etcdClient = Client.builder().endpoints(cacheConfig.getEtcdAddr()).build();
        }
        Watch watch = etcdClient.getWatchClient();
        ByteSequence key1 = ByteSequence.from(cacheConfig.getEtcdCachePath(), StandardCharsets.UTF_8);
        watch.watch(key1, watchResponse -> watchResponse.getEvents().forEach(event -> {
            String value = event.getKeyValue().getValue().toString();
            V newCacheValue = this.refresh(value);
            this.setCache(value, newCacheValue);
        }));
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
        KV kv = etcdClient.getKVClient();
        ByteSequence etcdKey = ByteSequence.from(getCacheConfig().getEtcdCachePath(), StandardCharsets.UTF_8);
        ByteSequence etcdValue = ByteSequence.from(key, StandardCharsets.UTF_8);
        // 写入key value
        kv.put(etcdKey, etcdValue);
    }
}
