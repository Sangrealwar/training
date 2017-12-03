package study.designPattem.memento;

/**
 * 名称：备忘者模式，
 * 功能：
 * 条件：
 * Created by wq on 2017/12/3.
 */
public class Client {

    public static void main(String[] args) {
        GameRole xbb= new GameRole("小冰冰");
        System.out.println("游戏开始");
        xbb.init();
        xbb.displayState();

        //请求保存一下进度
        RoleStateCaretaker caretaker =new RoleStateCaretaker();
        caretaker.setMemento(xbb.saveState());

        System.out.println("战斗开始");
        xbb.fight();
        xbb.displayState();

        System.out.println("读个档");
        xbb.recoveryState(caretaker.getMemento());
        xbb.displayState();

        System.out.println("游戏结束");
    }
}
