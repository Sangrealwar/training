package study.zookeeper.BarrierAndQueue;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * 功能：
 * 条件：
 * Created by wq on 2017/4/22.
 */
public class Queue extends SyncPrimitive {

    Queue(String address, String name) {
        super(address);
        this.root = name;
        // Create ZK node name
        if (zk != null) {
            try {
                Stat s = zk.exists(root, false);
                if (s == null) {
                    zk.create(root, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE,
                            CreateMode.PERSISTENT);
                }
            } catch (KeeperException e) {
                System.out.println("Zookeeper初始化队列报错，信息如下：" + e.getMessage());
            } catch (InterruptedException e) {
                System.out.println("中断错误");
            }
        }
    }

    /**
     * 生产，添加元素到队列中
     * @param i
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    boolean produce(int i) throws KeeperException, InterruptedException{
        ByteBuffer b = ByteBuffer.allocate(4);
        byte[] value;

        // 添加i
        b.putInt(i);
        value = b.array();
        String nodeName = zk.create(root + "/element", value, ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT_SEQUENTIAL);   //持久化_序列化，创建一个10位数的自增序号

        System.out.println("创建"+nodeName);

        return true;
    }

    /**
     * 消费
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    int consume() throws KeeperException, InterruptedException{
        int retvalue = -1;
        Stat stat = null;

        // 获得第一个元素
        while (true) {
            synchronized (mutex) {
                List<String> list = zk.getChildren(root, true);
                if (list.size() == 0) {
                    System.out.println("没有数据，等待");
                    mutex.wait();
                } else {
                    Integer min = new Integer(list.get(0).substring(7));
                    for(String s : list){
                        Integer tempValue = new Integer(s.substring(7));
                        //System.out.println("Temporary value: " + tempValue);
                        if(tempValue < min) min = tempValue;
                    }
                    String str ="0000000000";   //补齐10位
                    String eleName =str.substring(0, 10-String.valueOf(min).length())+String.valueOf(min);
                    System.out.println("临时节点: " + root + "/element" + eleName);
                    byte[] b = zk.getData(root + "/element" + eleName,
                            false, stat);
                    zk.delete(root + "/element" + eleName, 0);
                    ByteBuffer buffer = ByteBuffer.wrap(b);
                    retvalue = buffer.getInt();

                    return retvalue;
                }
            }
        }
    }
}
