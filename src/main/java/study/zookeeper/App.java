package study.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import study.zookeeper.lock.org.apache.zookeeper.recipes.lock.WriteLock;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * watch test
 */
public class App implements Watcher {
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    public static Stat stat = new Stat();
    ZooKeeper zooKeeper;

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        String p = "/app1/testaa";
        String p1 = "/app1/p1";
        String p2 = "/app2/p2";
        String p3 = "/app2/p3";
        //给zookeeper注入一个watch
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000,new App());
        connectedSemaphore.await();
        String p1Path = zooKeeper.create(p1, "456".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL);
        //TODO  第一个节点触发watch，给

        //exists register watch
        zooKeeper.exists(p1Path, new App());

        //get register watch
//        zooKeeper.getData(path, true, stat);
//        zooKeeper.setData(path, "hhhh".getBytes(), -1);
//        zooKeeper.exists(path, true);
//        exists register watch
//        zooKeeper.delete(path, -1);
        zooKeeper.delete(p1Path, -1);
        zooKeeper.delete("/app2/p2", -1);

    }

    //Watch触发是异步的
    public void process(WatchedEvent event) {
        if (zooKeeper != null) {
            try {
                zooKeeper.exists("/app2/p2", new App());
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        LOG.info("触发Watch的节点：" + event.getPath() + ";Watch类型为：" + event.getType() + ";Watch状态" + event.getState().toString());
        if (Event.KeeperState.SyncConnected == event.getState()) {
            if (Event.EventType.None == event.getType() && null == event.getPath()) {
                connectedSemaphore.countDown();
                System.out.println("zookeeper会话建立");
            } else if (Event.EventType.NodeCreated == event.getType()) {
                System.out.println("成功创建节点");

            } else if (Event.EventType.NodeDataChanged == event.getType()) {
                System.out.println("成功修改节点: " + event.getPath());

            } else if (Event.EventType.NodeDeleted == event.getType()) {
                System.out.println("成功删除节点");

            } else if (Event.EventType.NodeChildrenChanged == event.getType()) {
                System.out.println("子节点修改");

            }

        }
    }
}