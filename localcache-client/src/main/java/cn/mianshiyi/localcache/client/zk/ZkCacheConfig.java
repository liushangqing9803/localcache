package cn.mianshiyi.localcache.client.zk;


import cn.mianshiyi.localcache.client.AbstractCacheConfig;

/**
 * @author shangqing.liu
 */
public class ZkCacheConfig implements AbstractCacheConfig {
    //cache初始数
    private int initialCapacity;
    //cache最大缓存数
    private int maximumSize;
    //cache设置写缓存后n秒钟过期
    private int expireAfterWrite;

    //zk配置 缓存监听路径
    private String zkCachePath;
    //zk配置 地址
    private String zkAddr;

    public ZkCacheConfig(int initialCapacity, int maximumSize, int expireAfterWrite, String zkCachePath, String zkAddr) {
        this.initialCapacity = initialCapacity;
        this.maximumSize = maximumSize;
        this.expireAfterWrite = expireAfterWrite;
        this.zkCachePath = zkCachePath;
        this.zkAddr = zkAddr;
    }

    public ZkCacheConfig() {
    }

    public int getInitialCapacity() {
        return initialCapacity;
    }

    public void setInitialCapacity(int initialCapacity) {
        this.initialCapacity = initialCapacity;
    }

    public int getMaximumSize() {
        return maximumSize;
    }

    public void setMaximumSize(int maximumSize) {
        this.maximumSize = maximumSize;
    }

    public int getExpireAfterWrite() {
        return expireAfterWrite;
    }

    public void setExpireAfterWrite(int expireAfterWrite) {
        this.expireAfterWrite = expireAfterWrite;
    }

    public String getZkCachePath() {
        return zkCachePath;
    }

    public void setZkCachePath(String zkCachePath) {
        this.zkCachePath = zkCachePath;
    }

    public String getZkAddr() {
        return zkAddr;
    }

    public void setZkAddr(String zkAddr) {
        this.zkAddr = zkAddr;
    }

}
