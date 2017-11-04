import org.junit.Test

/**
 * 名称：
 * 功能：
 * 条件：
 * Created by wq on 2017/10/15.
 */
class StudentStudyTest {
    Student stu = new Student()
    @Test
    public void testStudyEvent(){
        stu.listener = [studentsStudy:{"students start study"}, studentsCome:{"students start come"}] as StudentListener
        assert stu.listenerState == null
        stu.study()
        assert stu.listenerState=="students start study"

        stu.listenerState = null

        stu.come()
        assert stu.listenerState == "students start come"
    }
}
