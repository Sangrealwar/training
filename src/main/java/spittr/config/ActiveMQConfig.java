package spittr.config;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import spittr.service.activemq.QueueMessageListener;
import spittr.service.activemq.TopicMessageListener;

/**
 * 功能：配置ActiveMQ的相关配置
 * 条件：
 * Created by wq on 2017/3/21.
 */
//@Configuration
//@ComponentScan(basePackages = {"spittr.activemq"})        //扫描，指定包
public class ActiveMQConfig {

    /**
     * ActiveMQ的连接工厂
     *
     * @return
     */
    @Bean
    public ActiveMQConnectionFactory amqConnectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setUserName("admin");
        activeMQConnectionFactory.setPassword("admin");
        activeMQConnectionFactory.setBrokerURL("tcp://localhost:61616");
        return activeMQConnectionFactory;
    }

    /**
     * 消息监听类
     *
     * @return
     */
    @Bean
    public QueueMessageListener queueMessageListener() {
        return new QueueMessageListener();
    }

    /**
     * jms的连接工厂，注入ActiveMQ的连接工厂
     *
     * @param amqConnectionFactory
     * @return
     */
    @Bean
    public CachingConnectionFactory connectionFactory(ActiveMQConnectionFactory amqConnectionFactory) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(amqConnectionFactory);
        connectionFactory.setSessionCacheSize(100);
        return connectionFactory;
    }

    /**
     * 配置JMS模板（Queue），Spring提供的JMS工具类，它发送、接收消息，同步发送消息
     *
     * @param demoQueueDestination
     * @param connectionFactory
     * @return
     */
    @Bean
    public JmsTemplate jmsTemplateQueue(ActiveMQQueue demoQueueDestination, CachingConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory);
        jmsTemplate.setDefaultDestination(demoQueueDestination);
        jmsTemplate.setReceiveTimeout(10000);
        //true是topic，false是queue，默认是false
        jmsTemplate.setPubSubNoLocal(false);
        return jmsTemplate;
    }

//    /**
//     * 消息监听容器
//     *
//     * @param demoQueueDestination
//     * @param connectionFactory
//     * @param queueMessageListener
//     * @return
//     */
//    @Bean
//    public DefaultMessageListenerContainer queueListenerContainer(
//            ActiveMQQueue demoQueueDestination,
//            PooledConnectionFactory connectionFactory,
//            QueueMessageListener queueMessageListener) {
//        DefaultMessageListenerContainer listenerContainer = new DefaultMessageListenerContainer();
//        listenerContainer.setConnectionFactory(connectionFactory);
//        listenerContainer.setDestination(demoQueueDestination);
//        listenerContainer.setMessageListener(queueMessageListener);
//        return listenerContainer;
//    }


    /**
     * 单播-队列式
     *
     * @return
     */
    @Bean
    public ActiveMQQueue demoQueueDestination() {
        ActiveMQQueue queue = new ActiveMQQueue("demo");
        return queue;
    }

    @Bean(destroyMethod = "stop")
    public PooledConnectionFactory pooledConnectionFactory(ActiveMQConnectionFactory amqConnectionFactory) {
        PooledConnectionFactory factory = new PooledConnectionFactory(amqConnectionFactory);
        factory.setMaxConnections(1);
        return factory;
    }

    @Bean
    public TopicMessageListener topicMessageListener() {
        return new TopicMessageListener();
    }

    /**
     * JMS-生产者主题
     *
     * @param demoTopic
     * @param pooledConnectionFactory
     * @return
     */
    @Bean
    public JmsTemplate jmsTemplateTopic(ActiveMQTopic demoTopic, PooledConnectionFactory pooledConnectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(pooledConnectionFactory);
        jmsTemplate.setDefaultDestination(demoTopic);
        jmsTemplate.setReceiveTimeout(10000);
        //true是topic，false是queue，默认是false
        jmsTemplate.setPubSubNoLocal(true);
        jmsTemplate.setDeliveryMode(1);
        return jmsTemplate;
    }

    /**
     * 使用消息容器异步接受topic消息
     * @param demoTopic
     * @param connectionFactory
     * @param topicMessageListener
     * @return
     */
    @Bean
    public DefaultMessageListenerContainer topicListenerContainer(
            ActiveMQTopic demoTopic,
            PooledConnectionFactory connectionFactory,
            TopicMessageListener topicMessageListener) {
        DefaultMessageListenerContainer listenerContainer = new DefaultMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory);
        listenerContainer.setDestination(demoTopic);
        listenerContainer.setMessageListener(topicMessageListener);
        return listenerContainer;
    }

    /**
     * 广播-主题式
     *
     * @return
     */
    @Bean
    public ActiveMQTopic demoTopic() {
        //设置的是physicalName，必填
        ActiveMQTopic topic = new ActiveMQTopic("demo");
        return topic;
    }
}
