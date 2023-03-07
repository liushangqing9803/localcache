package cn.mianshiyi.localcache.client.utils;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
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
            System.out.println(path + " create success!");
        } else {
            System.out.println(path + " is exists!");
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

    /**
     * 监听某个节点的修改和创建事件
     *
     * @param curatorFramework
     * @param path
     */
    private static void addListenerWithNodeCache(CuratorFramework curatorFramework, String path) throws Exception {
        curatorFramework.create().creatingParentContainersIfNeeded().forPath(path);
        NodeCache nodeCache = new NodeCache(curatorFramework, path, false);
        NodeCacheListener nodeCacheListener = () -> {
            ChildData currentData = nodeCache.getCurrentData();
            System.out.println("监听到节点事件: " + currentData.getPath() + " -> " + new String(currentData.getData()));
        };
        nodeCache.getListenable().addListener(nodeCacheListener);
        nodeCache.start();
    }
}
