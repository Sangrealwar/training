import org.junit.Test
import groovy.json.JsonSlurper

import java.util.logging.Logger

/**
 * 名称：
 * 功能：
 * 条件：
 * Created by wq on 2017/10/15.
 */
class GroovyApp {
    def names
    def age
    def birth

    void hello() {
        print(names + "," + age + "," + birth);
    }

    @Test
    void main() {
        def aa = new GroovyApp(names: "大笨妞", age: 18, birth: 2017 - 9 - 25);
        aa.hello()
    }
}
