package cn.mianshiyi.localcache.client;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.Objects;

/**
 * @author shangqing.liu
 */
public class ZookeeperUtil {
    /**
     * 创建持久化节点
     *
     * @param curatorFramework
     * @param path
     * @throws Exception
     */
    public static void createPersistent(CuratorFramework curatorFramework, String path) throws Exception {
        Stat stat = curatorFramework.checkExists().forPath(path);
        if (Objects.isNull(stat)) {
            curatorFramework.create().creatingParentContainersIfNeeded() // 如果指定节点的父节点不存在，则会自动级联创建父节点
                    .withMode(CreateMode.PERSISTENT).forPath(path, null);
        }
    }

    /**
     * 设置某个节点数据
     *
     * @param curatorFramework
     * @param path
     * @throws Exception
     */
    public static void setData(CuratorFramework curatorFramework, String path, String data) throws Exception {
        curatorFramework.setData().forPath(path, data.getBytes());
    }
}
