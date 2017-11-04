package study.event;

/**
 * 名称：嘲讽
 * 功能：
 * 条件：
 * Created by wq on 2017/7/29.
 */
public class TauntListener implements Listener<AttackEvent> {

    public void handleEvent(AttackEvent event) {
        System.out.println("来打我啊");
    }
}
