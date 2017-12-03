package study.designPattem.memento;

/**
 * 名称：角色状态守护者
 * 功能：
 * 条件：
 * Created by wq on 2017/12/3.
 */
public class RoleStateCaretaker {

    private RoleStateMemento memento;

    public RoleStateMemento getMemento() {
        return memento;
    }

    public void setMemento(RoleStateMemento memento) {
        this.memento = memento;
    }
}
