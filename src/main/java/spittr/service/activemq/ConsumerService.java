package spittr.service.activemq;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.TextMessage;

/**
 * 功能：消息的消费者，向消息队列中添加一条消息
 * 条件：
 * Created by wq on 2017/3/21.
 */
//@Service
public class ConsumerService {
    @Resource(name="jmsTemplateQueue")
    private JmsTemplate jmsTemplate;

    /**
     * 接收消息
     */
    public TextMessage receive(Destination destination) {
        TextMessage tm = (TextMessage) jmsTemplate.receive(destination);
            try {
                if(tm==null) {
                    System.out.println("消息队列中没有消息了");
                }
                else{
                System.out.println("从队列" + destination.toString() + "收到了消息：\t"
                        + tm.getText());
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }

        return tm;

    }
}
