package cn.mianshiyi.localcache.client.etcd;


import cn.mianshiyi.localcache.client.AbstractCacheConfig;

/**
 * @author shangqing.liu
 */
public class EtcdCacheConfig implements AbstractCacheConfig {
    //cache初始数
    private int initialCapacity;
    //cache最大缓存数
    private int maximumSize;
    //cache设置写缓存后n秒钟过期
    private int expireAfterWrite;

    //etcd配置 缓存监听路径
    private String etcdCachePath;
    //etcd 地址
    private String[] etcdAddr;

    public EtcdCacheConfig(int initialCapacity, int maximumSize, int expireAfterWrite, String etcdCachePath, String[] etcdAddr) {
        this.initialCapacity = initialCapacity;
        this.maximumSize = maximumSize;
        this.expireAfterWrite = expireAfterWrite;
        this.etcdCachePath = etcdCachePath;
        this.etcdAddr = etcdAddr;
    }

    public EtcdCacheConfig() {
    }

    @Override
    public int getInitialCapacity() {
        return initialCapacity;
    }

    public void setInitialCapacity(int initialCapacity) {
        this.initialCapacity = initialCapacity;
    }

    @Override
    public int getMaximumSize() {
        return maximumSize;
    }

    public void setMaximumSize(int maximumSize) {
        this.maximumSize = maximumSize;
    }

    @Override
    public int getExpireAfterWrite() {
        return expireAfterWrite;
    }

    public void setExpireAfterWrite(int expireAfterWrite) {
        this.expireAfterWrite = expireAfterWrite;
    }

    public String getEtcdCachePath() {
        return etcdCachePath;
    }

    public void setEtcdCachePath(String etcdCachePath) {
        this.etcdCachePath = etcdCachePath;
    }

    public String[] getEtcdAddr() {
        return etcdAddr;
    }

    public void setEtcdAddr(String[] etcdAddr) {
        this.etcdAddr = etcdAddr;
    }

}
