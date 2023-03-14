package cn.mianshiyi.localcache.client;


/**
 * @author shangqing.liu
 */
public class CacheConfig {
    //初始数
    private int initialCapacity;
    //cache最大缓存数
    private int maximumSize;
    //设置写缓存后n秒钟过期
    private int expireAfterWrite;

    //zk配置 缓存监听路径
    private String zkCachePath;
    //zk配置 地址
    private String zkAddr;

    //etcd配置 缓存监听路径
    private String etcdCachePath;
    //etcd 地址
    private String[] etcdAddr;


    public CacheConfig(int initialCapacity, int maximumSize, int expireAfterWrite) {
        this.initialCapacity = initialCapacity;
        this.maximumSize = maximumSize;
        this.expireAfterWrite = expireAfterWrite;
    }

    public CacheConfig() {
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

    public String[] getEtcdAddr() {
        return etcdAddr;
    }

    public void setEtcdAddr(String[] etcdAddr) {
        this.etcdAddr = etcdAddr;
    }

    public String getEtcdCachePath() {
        return etcdCachePath;
    }

    public void setEtcdCachePath(String etcdCachePath) {
        this.etcdCachePath = etcdCachePath;
    }
}
