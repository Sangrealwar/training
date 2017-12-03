package study.mq.activemq.request_reply;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Scanner;
import java.util.UUID;

/**
 * 功能：
 * 条件：
 * Created by wq on 2017/3/21.
 */
public class MessageClient {
    String                 brokerurl      = "ip";
    static Session session        = null;

    static MessageConsumer consumer       = null;
    static MessageProducer producer       = null;

    static TemporaryQueue temporaryQueue = null;

    public void setURL(String brokerurl) {
        this.brokerurl = brokerurl;
    }

    public void start() throws JMSException {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerurl);
        Connection connection = connectionFactory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue(Config.QUEUE_NAME);

        temporaryQueue = session.createTemporaryQueue();

        {
            producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            consumer = session.createConsumer(temporaryQueue);
            consumer.setMessageListener(new MMessageListener());
        }
    }

    public void request(String request) throws JMSException {
        System.out.println("REQUEST TO : " + request);

        TextMessage textMessage = session.createTextMessage();
        textMessage.setText(request);

        textMessage.setJMSReplyTo(temporaryQueue);

        textMessage.setJMSCorrelationID(UUID.randomUUID().toString());

        MessageClient.producer.send(textMessage);
    }

    private static class MMessageListener implements MessageListener {

        public void onMessage(Message message) {
            try {
                if (message instanceof TextMessage) {
                    String messageText = ((TextMessage) message).getText();
                    System.out.println("REPLY FROM : " + messageText.toUpperCase());
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

    // -----------------------------------------------------------------------

    public static void main(String[] args) {

        Config.introduce();
        Config.introduceClient();

        try {
            MessageClient client = new MessageClient();
            client.setURL("tcp://localhost:61616");

            Scanner scanner = new Scanner(System.in);
            client.start();
            {
                System.out.println("-----------------------------------------");
                System.out.println("|           NormalClient Start!               |");
                System.out.println("-----------------------------------------");
            }
            String message = "";
            int i = 0;

            while (!(message = scanner.next()).equals("exit")) {
                client.request(message + " : " + i++);
            }
            scanner.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
