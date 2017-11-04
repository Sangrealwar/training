package spittr.service.activemq;

/**
 * 功能：消息topic的监听，异步接收消息
 * 条件：
 * Created by wq on 2017/3/22.
 */
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;


public class TopicMessageListener implements MessageListener {


    //当收到消息后，自动调用该方法
    public void onMessage(Message message) {

        TextMessage tm = (TextMessage) message;
        try {
            System.out.println("topicMessageListener监听到了文本消息：\t"
                    + tm.getText());
            //do something ...
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}