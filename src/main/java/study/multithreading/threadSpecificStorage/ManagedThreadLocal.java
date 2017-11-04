package study.multithreading.threadSpecificStorage;


import java.lang.ref.WeakReference;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 名称：
 * 功能：
 * 条件：
 * Created by wq on 2017/9/9.
 */
public class ManagedThreadLocal<T> extends ThreadLocal<T> {

    /**
     * 使用弱引用，防止内存泄露。
     * 使用volatile修饰保证内存可见性
     */
    private static volatile Queue<WeakReference<ManagedThreadLocal<?>>> instances =
            new ConcurrentLinkedQueue<WeakReference<ManagedThreadLocal<?>>>();

    private volatile ThreadLocal<T> threadLocal;

    /**
     * 构造函数
     *
     * @param ivp
     */
    private ManagedThreadLocal(final InitialValueProvider<T> ivp) {
        this.threadLocal = new ThreadLocal<T>() {
            @Override
            protected T initialValue() {
                return ivp.initialValue();
            }
        };
    }

    public static <T> ManagedThreadLocal<T> newInstance(
            final InitialValueProvider<T> ivp) {
        ManagedThreadLocal<T> mtl = new ManagedThreadLocal<T>(ivp);

        // 使用弱引用来引用ThreadLocalProxy实例，防止内存泄漏。
        instances.add(new WeakReference<ManagedThreadLocal<?>>(mtl));
        return mtl;
    }

    public static <T> ManagedThreadLocal<T> newInstance() {
        return newInstance(new ManagedThreadLocal.InitialValueProvider<T>());
    }

    public T get() {
        return threadLocal.get();
    }

    public void set(T value) {
        threadLocal.set(value);
    }

    public void remove() {
        if (null != threadLocal) {
            threadLocal.remove();
            threadLocal = null;
        }
    }

    /**
     * 清理该类所管理的所有ThreadLocal实例。
     */
    public static void removeAll() {
        WeakReference<ManagedThreadLocal<?>> wrMtl;
        ManagedThreadLocal<?> mtl;
        while (null != (wrMtl = instances.poll())) {
            mtl = wrMtl.get();
            if (null != mtl) {
                mtl.remove();
            }
        }
    }

    public static class InitialValueProvider<T> {
        protected T initialValue() {

            //默认值为null
            return null;
        }
    }
}
