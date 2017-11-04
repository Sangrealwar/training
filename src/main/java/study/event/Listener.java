package study.event;

import java.util.EventListener;

/**
 * 名称： 监听
 * 功能：
 * 条件：
 * Created by wq on 2017/7/29.
 */
public interface Listener <T extends Event> extends EventListener {

    void handleEvent(T event);
}
