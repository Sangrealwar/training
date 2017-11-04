/**
 * 名称：
 * 功能：
 * 条件：
 * Created by wq on 2017/10/15.
 */
class Student {
    StudentListener listener;
    String listenerState
    void study(){
        if(listener){
            listenerState = listener.studentsStudy()
        }
    }
    void come(){
        if(listener){
            listenerState = listener.studentsCome()
        }
    }
}
