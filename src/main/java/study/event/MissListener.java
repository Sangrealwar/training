package study.event;

/**
 * 名称：丢失
 * 功能：
 * 条件：
 * Created by wq on 2017/7/29.
 */
public class MissListener implements Listener<AttackEvent> {

    public void handleEvent(AttackEvent event) {
        System.out.println("打不到");
    }
}

