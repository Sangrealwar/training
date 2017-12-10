package study.designPattem.command;

/**
 * 名称：烤鸡翅的命令
 * 功能：
 * 条件：
 * Created by wq on 2017/12/3.
 */
public class BakeChickenWingCommand extends Command {

    public BakeChickenWingCommand(Barbecuer barbecuer) {
        super(barbecuer);
    }

    @Override
    public void executeCommand() {
        barbecuer.bakeChickenWing();
    }
}
