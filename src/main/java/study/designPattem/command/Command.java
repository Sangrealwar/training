package study.designPattem.command;

/**
 * 名称：命令的抽象，封装了命令的执行对象
 * 功能：
 * 条件：
 * Created by wq on 2017/12/3.
 */
public abstract class Command {

    /**
     * 命令的对象
     */
    protected Barbecuer barbecuer;

    public Command(Barbecuer barbecuer) {
        this.barbecuer = barbecuer;
    }

    abstract public void executeCommand();


}
