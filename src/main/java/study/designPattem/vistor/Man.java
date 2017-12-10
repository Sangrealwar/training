package study.designPattem.vistor;

/**
 * 名称：
 * 功能：
 * 条件：
 * Created by wq on 2017/12/10.
 */
public class Man extends Persion {

    // 客户端将具体状态作为参数传递给"男人"，完成一次分派
    // 然后"男人"调用参数的"男人反应"，同时将自己传进入，完成二次分派
    // 双分派意味着执行操作决定请求的种类和两个接收者的类型
    // "接受"方法执行的方法不仅取决于"状态"的类型，还取决与访问的"人"的类型
    @Override
    public void accept(Action visitor) {
        visitor.getManConslusion(this);
    }
}
