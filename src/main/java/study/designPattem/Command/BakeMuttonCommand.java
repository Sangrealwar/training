package study.designPattem.Command;

/**
 * 名称：拷肉的命令
 * 功能：
 * 条件：
 * Created by wq on 2017/12/3.
 */
public class BakeMuttonCommand extends Command {

    public BakeMuttonCommand(Barbecuer barbecuer) {
        super(barbecuer);
    }

    @Override
    public void executeCommand() {
        barbecuer.bakeMutton();
    }
}
