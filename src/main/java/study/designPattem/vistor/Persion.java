package study.designPattem.vistor;

/**
 * 名称：人，接受访问状态的的接收人
 * 功能：
 * 条件：
 * Created by wq on 2017/12/10.
 */
public abstract class Persion {

    /**
     * 接受，获得访问对象
     *
     * @param visitor
     */
    public abstract void accept(Action visitor);
}
