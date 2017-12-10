package study.designPattem.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * 名称：代理类练习
 * 功能：
 * 条件：
 * Created by wq on 2017/7/30.
 */
public class ProxyTest {

    public static void main(String[] args) {

        Object[] elements = new Object[1000];

        for (int i = 0; i < elements.length; i++) {
            Integer value = i + 1;
            InvocationHandler handler = new TrackHandler(value);
            //代理-"比较"接口
            Object proxy = Proxy.newProxyInstance(null, new Class[]{Comparable.class}, handler);
            elements[i] = proxy;
        }

        //二分法要查找的数
        Integer key = 1;//new Random().nextInt(elements.length) + 1;
        //二分法
        int result = Arrays.binarySearch(elements, key);

        if (result > 0) System.out.println(elements[result]);
    }
}


/**
 * 代理类
 */
class TrackHandler implements InvocationHandler {
    private Object target;

    TrackHandler(Object target) {
        this.target = target;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.print(target);

        System.out.print("，" + method.getName() + "(");

        if (args != null) {

            for (int i = 0; i < args.length; i++) {
                System.out.print(args[i]);
                if (i < args.length - 1)
                    System.out.print("，");
            }
        }
        System.out.println(")");

        return method.invoke(target, args);
    }
}
