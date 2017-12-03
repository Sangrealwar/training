package study.mq.activemq.pub_sub;

import javax.jms.*;
import javax.jms.Destination;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Pub {
    private static final int SENDNUM = 10;// 发送消息的数量

    public static void main(String[] args) {
        ConnectionFactory connectionFactory;// 连接工厂
        Connection connection = null;// 连接
        Session session;// 会话 发送或者接受消息的线程
        Destination destination;// 消息的目的地
        MessageProducer messageProducer;// 消息发布者
        // 实例化连接工厂
        connectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD,
                "tcp://localhost:61616");

        try {
            // 通过连接工厂获取连接
            connection = connectionFactory.createConnection();
            // 启动连接
            connection.start();
            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            destination = session.createTopic("FirstTopic");// 创建消息队列
            messageProducer = session.createProducer(destination);// 创建消息生产者
            sendMessage(session, messageProducer);
            // 由于设置添加事务，这里需要使用提交才能将数据发送出去
            session.commit();

        } catch (JMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    // 发送消息
    public static void sendMessage(Session session, MessageProducer messageProducer) {
        for (int i = 0; i < Pub.SENDNUM; i++) {
            try {
                TextMessage message = session.createTextMessage("Active MQ发送消息" + i);
                System.out.println("发布消息：Active MQ发送消息");
                messageProducer.send(message);
            } catch (JMSException e) {
                // TODO Auto-generated catc h block
                e.printStackTrace();
            }
        }
    }
}
