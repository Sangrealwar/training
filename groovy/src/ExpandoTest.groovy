import org.junit.Test

/**
 * 名称：
 * 功能：
 * 条件：
 * Created by wq on 2017/10/15.
 */
class ExpandoTest {

    @Test
    void test() {
        def ex = new Expando()
        ex.name = 'XiaoMing'
        ex.speak = { "$name says Hello World!" }
        println ex.speak()
    }
}
