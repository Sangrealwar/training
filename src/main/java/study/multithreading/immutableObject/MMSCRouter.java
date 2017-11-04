package study.multithreading.immutableObject;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 名称：不可变对象来维护路由表-ImmutableObject
 * 功能：彩信中心路由规则管理器
 * 条件：
 * Created by wq on 2017/8/5.
 */
public final class MMSCRouter {

    //volatile修饰，保证多线程下该变量的可见性
    private static volatile MMSCRouter instance = new MMSCRouter();

    //初始化就构建好，维护手机号前缀到彩信中心的关系
    public final Map<String, MMSCInfo> routeMap;

    public MMSCRouter() {
        //从数据库中加载到内存中
        this.routeMap = MMSCRouter.retrieveRouteMapFromDB();
    }

    private static Map<String, MMSCInfo> retrieveRouteMapFromDB() {
        Map<String, MMSCInfo> map = new HashMap<String, MMSCInfo>();

        map.put("linlin", new MMSCInfo("A111", "333.333.33.33", 10));
        return map;
    }

    public static MMSCRouter getInstance() {
        return instance;
    }

    /**
     * 根据手机号前缀获取对应的彩信中心信息
     *
     * @param msisdnPrefix
     * @return
     */
    public MMSCInfo getMMSC(String msisdnPrefix) {
        return routeMap.get(msisdnPrefix);
    }

    /**
     * 更新MMSCRouter
     *
     * @param newInstance
     */
    public static void setInstance(MMSCRouter newInstance) {
        instance = newInstance;
    }

    private static Map<String, MMSCInfo> deepCopy(Map<String, MMSCInfo> m) {
        Map<String, MMSCInfo> result = new HashMap<String, MMSCInfo>();

        for (String key : result.keySet()) {
            result.put(key, new MMSCInfo(m.get(key)));
        }

        return result;
    }

    public Map<String, MMSCInfo> getRouteMap() {
        //做防御性复制
        return Collections.unmodifiableMap(deepCopy(routeMap));
    }
}
