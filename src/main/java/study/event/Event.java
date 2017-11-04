package study.event;

import java.util.EventObject;

/**
 * 名称： 事件
 * 功能：
 * 条件：
 * Created by wq on 2017/7/29.
 */
public class Event extends EventObject {

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public Event(Object source) {
        super(source);
    }
}
