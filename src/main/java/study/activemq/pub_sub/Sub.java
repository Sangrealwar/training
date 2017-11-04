package study.activemq.pub_sub;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 订阅者要先启动
 */
public class Sub {

    public static void main(String[] args) {
        ConnectionFactory connectionFactory; // 连接工厂
    Connection connection = null; // 连接
    Session session; // 会话 接受或者发送消息的线程
    Destination destination; // 消息的目的地
    MessageConsumer messageConsumer; // 消息的订阅者


    connectionFactory = new ActiveMQConnectionFactory(
                        ActiveMQConnection.DEFAULT_USER,
                        ActiveMQConnection.DEFAULT_PASSWORD,
                        "tcp://localhost:61616");
        try {
        // 通过连接工厂获取连接
        connection=connectionFactory.createConnection();  // 通过连接工厂获取连接
        connection.start(); // 启动连接
        session=connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE); // 创建Session
        destination=session.createTopic("FirstTopic");
        messageConsumer=session.createConsumer(destination); // 创建消息消费者
        messageConsumer.setMessageListener(new Listener()); // 注册消息监听

    } catch (JMSException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
}
}