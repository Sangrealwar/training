package study.multithreading.promise;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 名称：
 * 功能：
 * 条件：
 * Created by wq on 2017/7/29.
 */
public class Client {

    public static void main(String[] args) {
        //一堆装备
        List<Equipment> equipments = new ArrayList<Equipment>();

        //有一个信使
        Chick chick = new Chick();

        //小鸡承诺送拿一件装备
        Promise<Equipment> promise = chick.delivery();

        System.out.println("SF：打怪升级去了");

        Equipment equipment = null;
        try {
            equipment = (Equipment) promise.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("SF：拿到了" + equipment.getName());
    }
}
