package cn.mianshiyi.localcache.client;

/**
 * @author shangqing.liu
 */
public interface AbstractLocalCache<K,V> {

    V refresh(K key);

    V getCache(K key);

    V setCache(K key, V value);
}
