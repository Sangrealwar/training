package study.designPattem.vistor;

import java.text.MessageFormat;

/**
 * 名称：成功
 * 功能：
 * 条件：
 * Created by wq on 2017/12/10.
 */
public class Success extends Action {
    @Override
    public void getManConslusion(Man concreteElementA) {
        System.out.println(MessageFormat.format("{0} {1}时，背后多半有一个伟大的女人",
                concreteElementA.getClass().getSimpleName(), this.getClass().getSimpleName()));
    }

    @Override
    public void getWomanConslusion(Woman concreteElementB) {
        System.out.println(MessageFormat.format("{0} {1}时，背后多半有一个不成功的男人",
                concreteElementB.getClass().getSimpleName(), this.getClass().getSimpleName()));
    }
}
