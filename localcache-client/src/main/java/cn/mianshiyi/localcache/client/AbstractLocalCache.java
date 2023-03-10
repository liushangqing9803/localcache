package cn.mianshiyi.localcache.client;

import java.util.Map;

/**
 * @author shangqing.liu
 */
public interface AbstractLocalCache<K, V> {

    CacheConfig getCacheConfig();

    V refresh(String key);

    V getCache(K key);

    V setCache(K key, V value);
}
