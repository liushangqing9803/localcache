package cn.mianshiyi.localcache.client;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * 使用jgroup作为广播渠道
 * 来更新各client中的缓存
 * 默认采用caffeine cache 作为缓存使用
 * 实现类需要 CacheConfig 配置类中需要传入jgroupGroupName
 * 实现类项目依赖spring，需要注入到spring中
 *
 * @author shangqing.liu
 */
public abstract class JGroupAbstractLocalCache<V> extends ReceiverAdapter implements AbstractLocalCache<V>, Broadcast {

    private Cache<String, V> CACHE;

    private JChannel channel;

    @PostConstruct
    public final void init() throws Exception {
        CacheConfig cacheConfig = getCacheConfig();
        CACHE = Caffeine.newBuilder()
                //cache的初始容量
                .initialCapacity(cacheConfig.getInitialCapacity())
                //cache最大缓存数
                .maximumSize(cacheConfig.getMaximumSize())
                //设置写缓存后n秒钟过期
                .expireAfterWrite(cacheConfig.getExpireAfterWrite(), TimeUnit.SECONDS)
                .build();

        if (channel == null) {
            // 创建JChannel对象，指定唯一的群组名称和配置文件
            channel = new JChannel("udp.xml");
            // 设置Receiver对象
            channel.setReceiver(this);
            // 连接到群组
            channel.connect(cacheConfig.getJgroupGroupName());
        }
    }

    @Override
    public final V getCache(String key) {
        return CACHE.get(key, k -> refresh(key));
    }


    @Override
    public final V setCache(String key, V value) {
        CACHE.put(key, value);
        return value;
    }

    @Override
    public final void broadcast(String key) {
        try {
            // 发送消息
            Message msg = new Message(null, key);
            channel.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receive(Message msg) {
        // 接收消息
        String cacheKey = msg.getObject().toString();
        V newCacheValue = this.refresh(cacheKey);
        this.setCache(cacheKey, newCacheValue);
    }
}
