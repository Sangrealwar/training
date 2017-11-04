package spittr.service.activemq;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * 功能：
 * 条件：
 * Created by wq on 2017/7/22.
 */
//@Service
public class TopicProducerService {
    @Resource(name="jmsTemplateTopic")
    private JmsTemplate jmsTemplate;

    /**
     * 向指定主题发送消息
     */
    public void sendMessage(Destination destination, final String msg) {
        System.out.println("向" + destination.toString() + "发布了主题------------" + msg);
        jmsTemplate.send(destination, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(msg);
            }
        });
    }

    /**
     * 向默认主题发送消息
     */
    public void sendMessage(final String msg) {
        String destination =  jmsTemplate.getDefaultDestination().toString();
        System.out.println("向" + destination.toString() + "发布了主题------------" + msg);
        jmsTemplate.send(new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(msg);
            }
        });
    }

}
