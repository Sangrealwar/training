package study.event;

/**
 * 功能： 事件源
 * 条件：
 * Created by wq on 2017/7/29.
 */
public interface EventSource {

    void register(Listener listener);

    void fireEvent(Event event);
}
