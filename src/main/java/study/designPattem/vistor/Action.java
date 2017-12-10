package study.designPattem.vistor;

/**
 * 名称： 状态（访问者）
 * 功能：
 * 条件：
 * Created by wq on 2017/12/10.
 */
public abstract class Action {

    public abstract void getManConslusion(Man concreteElementA);

    public abstract void getWomanConslusion(Woman concreteElementB);
}
