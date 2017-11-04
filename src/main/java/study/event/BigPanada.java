package study.event;

import java.util.ArrayList;
import java.util.List;

/**
 * 名称：大熊猫（dota英雄）
 * 功能：
 * 条件：
 * Created by wq on 2017/7/29.
 */
public class BigPanada implements EventSource {

    private List<Listener> listeners = new ArrayList<Listener>();


    public void register(Listener listener) {
        listeners.add(listener);
    }

    public void fireEvent(Event event) {
        for (Listener listener : listeners) {
            try {
                listener.handleEvent(event);
            } catch (ClassCastException ex) {
//                ex.printStackTrace();
            }
        }
    }
}
