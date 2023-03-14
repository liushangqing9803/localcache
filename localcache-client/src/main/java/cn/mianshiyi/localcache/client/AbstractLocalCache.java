package cn.mianshiyi.localcache.client;

/**
 * @author shangqing.liu
 */
public interface AbstractLocalCache<V> {

    CacheConfig getCacheConfig();

    V refresh(String key);

    V getCache(String key);

    V setCache(String key, V value);
}
