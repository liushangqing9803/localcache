package cn.mianshiyi.localcache.client;


/**
 * @author shangqing.liu
 */
public interface Broadcast {
    void broadcast(byte[] data);

    void receive(byte[] data);

}
