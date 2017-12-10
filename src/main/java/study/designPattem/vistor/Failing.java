package study.designPattem.vistor;

import java.text.MessageFormat;

/**
 * 名称：
 * 功能：
 * 条件：
 * Created by wq on 2017/12/10.
 */
public class Failing extends Action {
    @Override
    public void getManConslusion(Man concreteElementA) {
        System.out.println(MessageFormat.format("{0} {1}时，倒霉咯",
                concreteElementA.getClass().getSimpleName(), this.getClass().getSimpleName()));
    }

    @Override
    public void getWomanConslusion(Woman concreteElementB) {
        System.out.println(MessageFormat.format("{0} {1}时，怪我咯",
                concreteElementB.getClass().getSimpleName(), this.getClass().getSimpleName()));
    }
}
