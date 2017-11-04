package study.event;

/**
 * 名称：攻击事件
 * 功能：
 * 条件：
 * Created by wq on 2017/7/29.
 */
public class AttackEvent extends Event {

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public AttackEvent(Object source) {
        super(source);
    }
}
