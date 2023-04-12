package cn.mianshiyi.localcache.client.jgroup;


import cn.mianshiyi.localcache.client.AbstractCacheConfig;

/**
 * @author shangqing.liu
 */
public class JGroupCacheConfig implements AbstractCacheConfig {
    //cache初始数
    private int initialCapacity;
    //cache最大缓存数
    private int maximumSize;
    //cache设置写缓存后n秒钟过期
    private int expireAfterWrite;
    //Jjgroup配置 缓存监听路径
    private String jGroupGroupName;

    public JGroupCacheConfig(int initialCapacity, int maximumSize, int expireAfterWrite, String jGroupGroupName) {
        this.initialCapacity = initialCapacity;
        this.maximumSize = maximumSize;
        this.expireAfterWrite = expireAfterWrite;
        this.jGroupGroupName = jGroupGroupName;
    }

    public JGroupCacheConfig() {
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

    public String getjGroupGroupName() {
        return jGroupGroupName;
    }

    public void setjGroupGroupName(String jGroupGroupName) {
        this.jGroupGroupName = jGroupGroupName;
    }

}
