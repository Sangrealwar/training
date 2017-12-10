package study.designPattem.command;

/**
 * 名称：
 * 功能：
 * 条件：
 * Created by wq on 2017/12/3.
 */
public class Client {

    public static void main(String[] args) {

        Barbecuer boy = new Barbecuer("烤肉师傅：大雄");

        Command bakeMutton = new BakeMuttonCommand(boy);
        Command bakeMutton1 = new BakeMuttonCommand(boy);
        Command bakeChickenWing = new BakeChickenWingCommand(boy);

        Waiter girl = new Waiter();

        girl.setOrder(bakeMutton);
        girl.setOrder(bakeMutton1);
        girl.setOrder(bakeChickenWing);

        girl.cancelCommand(bakeChickenWing);

        girl.notifyCommand();

        System.out.println("做完啦");
    }


}
