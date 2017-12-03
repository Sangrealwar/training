package study.designPattem.memento;

/**
 * 名称：角色状态箱，
 * 功能：
 * 条件：
 * Created by wq on 2017/12/3.
 */
public class RoleStateMemento {

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

    public RoleStateMemento(int attack, int defence, int health) {
        this.attack = attack;
        this.defence = defence;
        this.health = health;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefence() {
        return defence;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
