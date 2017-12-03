package study.designPattem.Command;

/**
 * 名称：烤肉师傅
 * 功能：
 * 条件：
 * Created by wq on 2017/12/3.
 */
public class Barbecuer {
    private String name;

    public Barbecuer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void bakeMutton() {
        System.out.println("烤肉中--------");
    }

    public void bakeChickenWing() {
        System.out.println("拷鸡翅中------");
    }
}
