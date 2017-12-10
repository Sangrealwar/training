package study.designPattem.vistor;

/**
 * 名称：
 * 功能：访问者把数据结构和在结构上的操作解耦开，如果状态有很多中，但数据结构
 * 条件：
 * Created by wq on 2017/12/10.
 */
public class Client {
    public static void main(String[] args) {
        ObjectStructure objectStructure = new ObjectStructure();

        objectStructure.attach(new Man());
        objectStructure.attach(new Woman());

        Success v1 = new Success();
        objectStructure.display(v1);

        Failing v2 = new Failing();
        objectStructure.display(v2);
    }
}
