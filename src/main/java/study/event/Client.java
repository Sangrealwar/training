package study.event;

/**
 * 名称：天辉下路的一波gank
 * 功能：
 * 条件：
 * Created by wq on 2017/7/29.
 */
public class Client {


    public static void main(String[] args) {

        Nevermore sf = new Nevermore();

        BigPanada panada = new BigPanada();

        System.out.println("大熊猫学习了闪避和嘲讽两个技能");
        panada.register(new MissListener());
        panada.register(new TauntListener());

        System.out.println("SF发动了攻击");
        AttackEvent attackEvent = new AttackEvent(sf);

        System.out.println("大熊猫受到了攻击");
        panada.fireEvent(attackEvent);

        System.out.println("gank结束");
    }
}
