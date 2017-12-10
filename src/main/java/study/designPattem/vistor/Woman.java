package study.designPattem.vistor;

/**
 * 名称：
 * 功能：
 * 条件：
 * Created by wq on 2017/12/10.
 */
public class Woman extends Persion {
    @Override
    public void accept(Action visitor) {
        visitor.getWomanConslusion(this);
    }
}
