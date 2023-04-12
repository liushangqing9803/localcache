package cn.mianshiyi.localcache.client.jgroup;


import cn.mianshiyi.localcache.client.AbstractLocalCache;
import cn.mianshiyi.localcache.client.Broadcast;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;

import java.util.concurrent.TimeUnit;

/**
 * 使用jgroup作为广播渠道
 * 来更新各client中的缓存
 * 默认采用caffeine cache 作为缓存使用
 * 实现类需要 CacheConfig 配置类中需要传入jgroupGroupName
 * @author shangqing.liu
 */
public abstract class JGroupAbstractLocalCache<K, V> extends ReceiverAdapter implements AbstractLocalCache<K, V>, Broadcast {

    private JGroupCacheConfig cacheConfig;

    private Cache<K, V> cache;

    private JChannel channel;

    public final void init(JGroupCacheConfig cacheConfig) {
        this.cacheConfig = cacheConfig;
        cache = Caffeine.newBuilder()
                //cache的初始容量
                .initialCapacity(cacheConfig.getInitialCapacity())
                //cache最大缓存数
                .maximumSize(cacheConfig.getMaximumSize())
                //设置写缓存后n秒钟过期
                .expireAfterWrite(cacheConfig.getExpireAfterWrite(), TimeUnit.SECONDS)
                .build();
        try {
            if (channel == null) {
                // 创建JChannel对象，指定唯一的群组名称和配置文件
                channel = new JChannel("udp.xml");
                // 设置Receiver对象
                channel.setReceiver(this);
                // 连接到群组
                channel.connect(cacheConfig.getjGroupGroupName());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public final V getCache(K key) {
        return cache.get(key, k -> refresh(key));
    }

    @Override
    public final V setCache(K key, V value) {
        cache.put(key, value);
        return value;
    }

    @Override
    public final void broadcast(byte[] data) {
        try {
            // 发送消息
            Message msg = new Message(null, data);
            channel.send(msg);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void receive(Message msg) {
        // 接收消息
        byte[] buffer = msg.getBuffer();
        receive(buffer);
    }

}
