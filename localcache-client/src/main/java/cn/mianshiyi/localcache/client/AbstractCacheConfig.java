package cn.mianshiyi.localcache.client;


/**
 * @author shangqing.liu
 */
public interface AbstractCacheConfig {

    int getInitialCapacity();

    int getMaximumSize();

    int getExpireAfterWrite();

}
