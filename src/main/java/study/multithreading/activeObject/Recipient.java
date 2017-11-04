package study.multithreading.activeObject;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 名称：容器对象
 * 功能：
 * 条件：
 * Created by wq on 2017/9/4.
 */
public class Recipient implements Serializable {
    private static final long serialVersionUID = -5427696559429827584L;
    private Set<String> to = new HashSet<String>();

    public void addTo(String msisdn) {
        to.add(msisdn);
    }

    public Set<String> getToList() {
        return (Set<String>) Collections.unmodifiableCollection(to);
    }
}
