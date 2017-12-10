package study.designPattem.vistor;

import java.util.ArrayList;
import java.util.List;

/**
 * 名称：对象结构
 * 功能：
 * 条件：
 * Created by wq on 2017/12/10.
 */
public class ObjectStructure {
    private List<Persion> elements = new ArrayList<>();

    /**
     * 增加
     *
     * @param element
     */
    public void attach(Persion element) {
        elements.add(element);
    }

    /**
     * 移除
     *
     * @param element
     */
    public void remove(Persion element) {
        elements.remove(element);
    }

    /**
     * 显示
     * @param visitor
     */
    public void display(Action visitor) {
        for (Persion one : elements) {
            one.accept(visitor);
        }
    }
}
