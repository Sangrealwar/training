package study.designPattem.memento;

/**
 * 名称：
 * 功能：
 * 条件：
 * Created by wq on 2017/12/3.
 */
public class GameRole {

    /**
     * 角色名
     */
    private String name;

    /**
     * 攻击力
     */
    private int attack;

    /**
     * 防御力
     */
    private int defence;

    /**
     * 生命值
     */
    private int health;

    /**
     * 保存角色状态
     *
     * @return
     */
    public RoleStateMemento saveState() {
        return new RoleStateMemento(attack, defence, health);
    }

    /**
     * 回复角色状态
     *
     * @param memento
     */
    public void recoveryState(RoleStateMemento memento) {
        this.attack = memento.getAttack();
        this.defence = memento.getDefence();
        this.health = memento.getHealth();
    }

    /**
     * 出门装
     */
    public void init() {
        this.attack = 100;
        this.defence = 100;
        this.health = 100;
    }

    /**
     * pk
     */
    public void fight(){
        this.attack = 1;
        this.defence = 1;
        this.health = 1;
    }

    /**
     * 面板显示
     */
    public void displayState() {
        System.out.println("\t攻击力：" + attack);
        System.out.println("\t防御力：" + defence);
        System.out.println("\t生命值：" + health);
    }

    public GameRole(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
