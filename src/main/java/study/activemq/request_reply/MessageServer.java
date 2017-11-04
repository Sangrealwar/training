package study.activemq.request_reply;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;

public class MessageServer {

    String                 brokerurl     = "ip";

    static BrokerService   brokerService = null;

    static MessageConsumer consumer      = null;
    static MessageProducer producer      = null;

    static Session         session       = null;

    public void setURL(String brokerurl) {
        this.brokerurl = brokerurl;
    }

    public void start() throws JMSException {
        createBroker(brokerurl);
        setConsumer(brokerurl);
    }

    public void createBroker(String brokerurl) {
        try {
            brokerService = new BrokerService();
            brokerService.setUseJmx(false);
            brokerService.setPersistent(false);
            brokerService.addConnector(brokerurl);
            brokerService.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setConsumer(String url) throws JMSException {

        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue(Config.QUEUE_NAME);

        {
            producer = session.createProducer(null);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            consumer = session.createConsumer(destination);
            consumer.setMessageListener(new MMessageListener());
        }
    }

    private static class MMessageListener implements MessageListener {

        public void onMessage(Message message) {
            try {
                TextMessage response = session.createTextMessage();
                if (message instanceof TextMessage) {
                    String messageText = ((TextMessage) message).getText();
                    response.setText(handleMessage(messageText));
                    System.out.println("REQUEST FROM " + messageText.toUpperCase());
                }
                response.setJMSCorrelationID(message.getJMSCorrelationID());
                producer.send(message.getJMSReplyTo(), response);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }

        private String handleMessage(String text) {
            return "RESPONSE TO " + text.toUpperCase();
        }
    }

    // -----------------------------------------------------------------
    public static void main(String[] args) {

        Config.introduce();
        Config.introduceServer();

        try {
            MessageServer server = new MessageServer();
            server.setURL("tcp://localhost:61616");
            server.start();
            {
                System.out.println("-----------------------------------------");
                System.out.println("|           Server Start!               |");
                System.out.println("-----------------------------------------");
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
