package cn.mianshiyi.localcache.client;

import java.util.Map;

/**
 * @author shangqing.liu
 */
public interface AbstractLocalCache<K, V> {

    Map<K, V> cache();

    String getType();

    V refresh(String key) throws Exception;

    V getCache(K key);

    void broadcast(K key) throws Exception;

    V setCache(K key, V value);

    void broadcastClient(K key) throws Exception;
}
