package study.zookeeper.BarrierAndQueue;


import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * 功能：
 * 条件：
 * Created by wq on 2017/4/22.
 */
public class Barrier extends SyncPrimitive {
    int size;
    String name;

    Barrier(String address,String root,int size,String name) {
        super(address);
        this.root = root;
        this.size = size;
        this.name=name;

        //创建Barrier节点
        if(zk == null)
        {
            try {
                Stat s = zk.exists(root, false);
                if (s == null) {
                    zk.create(root, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
            } catch (KeeperException e) {
                System.out.println("Zookeeper初始化队列报错，信息如下：" + e.getMessage());
            } catch (InterruptedException e) {
                System.out.println("中断错误");
            }
        }
        //节点名字
//        try
//        {
//            name = new String (InetAddress.getLocalHost().getCanonicalHostName().toString());
//        }
//        catch (UnknownHostException e) {
//            System.out.println(e.getMessage());
//        }
    }


    /**
     * 加入Barrier
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    boolean enter() throws KeeperException, InterruptedException{
        String a = zk.create(root + "/" + name, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL); //.EPHEMERAL_SEQUENTIAL);
        while (true) {
            synchronized (mutex) {
                List<String> list = zk.getChildren(root, true);

                if (list.size() < size) {
                    mutex.wait();
                } else {
                    return true;
                }
            }
        }
    }

    /**
     * 退出barrier
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    boolean leave() throws KeeperException, InterruptedException{
        zk.delete(root + "/" + name, 0);         //创建时自增序号，删除时name没有带序号，报错！
        while (true) {
            synchronized (mutex) {
                List<String> list = zk.getChildren(root, true);
                if (list.size() > 0) {
                    mutex.wait();
                } else {
                    return true;
                }
            }
        }
    }
}
