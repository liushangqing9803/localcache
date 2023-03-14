package cn.mianshiyi.localcache.client;

import org.checkerframework.checker.units.qual.K;

import java.util.Map;

/**
 * @author shangqing.liu
 */
public interface AbstractLocalCache<V> {

    CacheConfig getCacheConfig();

    V refresh(String key);

    V getCache(String key);

    V setCache(String key, V value);
}
