package study.multithreading.serialThredConfinement;

/**
 * 名称：
 * 功能：任务处理的抽象
 * 条件：
 * Created by wq on 2017/9/16.
 */
public interface TaskProcessor<T, V> {
    /**
     * 对指定任务进行处理
     *
     * @param task 任务
     * @return 任务处理结果
     * @throws Exception
     */
    V doProcess(T task) throws Exception;

}
