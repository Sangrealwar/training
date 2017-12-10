package study.designPattem.command;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 名称：服务员
 * 功能：
 * 条件：
 * Created by wq on 2017/12/3.
 */
public class Waiter {

    public List<Command> commands = new ArrayList<>();

    public void setOrder(Command command) {
        if (command instanceof BakeChickenWingCommand) {
            System.out.println("鸡翅没有了，换一个吧");
        } else {
            commands.add(command);
            System.out.println("增加订单：" + command.getClass() + "，时间：" + LocalDate.now() +" "+ LocalTime.now());
        }
    }

    public void cancelCommand(Command command) {
        commands.remove(command);
        System.out.println("取消订单：" + command.getClass() + "，时间：" + LocalDateTime.now());
    }

    public void notifyCommand() {
        for (Command command : commands) {
            command.executeCommand();
        }
    }
}
