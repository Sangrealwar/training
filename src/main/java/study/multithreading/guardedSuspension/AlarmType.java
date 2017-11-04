package study.multithreading.guardedSuspension;

/**
 * 名称：
 * 功能：告警类型
 * 条件：
 * Created by wq on 2017/8/6.
 */
public enum AlarmType {

    FAULT("fault"),
    RESUME("resume");

    private final String name;

    private AlarmType(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
