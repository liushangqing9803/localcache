package cn.mianshiyi.localcache.client.etcd;


import cn.mianshiyi.localcache.client.AbstractLocalCache;
import cn.mianshiyi.localcache.client.Broadcast;
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
 * 使用etcd作为广播渠道
 * 来更新各client中的缓存
 * 默认采用caffeine cache 作为缓存使用
 * 实现类需要 CacheConfig 配置类中需要传入etcdAddr以及监听路径etcdCachePath
 * @author shangqing.liu
 */
public abstract class EtcdAbstractLocalCache<K, V> implements AbstractLocalCache<K, V>, Broadcast {

    private EtcdCacheConfig cacheConfig;

    private Cache<K, V> cache;

    private Client etcdClient;

    @PostConstruct
    public final void init(EtcdCacheConfig cacheConfig) {
        this.cacheConfig = cacheConfig;
        cache = Caffeine.newBuilder()
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
            this.receive(value.getBytes(StandardCharsets.UTF_8));
        }));
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
            KV kv = etcdClient.getKVClient();
            ByteSequence etcdKey = ByteSequence.from(cacheConfig.getEtcdCachePath(), StandardCharsets.UTF_8);
            ByteSequence etcdValue = ByteSequence.from(data);
            // 写入key value
            kv.put(etcdKey, etcdValue);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
