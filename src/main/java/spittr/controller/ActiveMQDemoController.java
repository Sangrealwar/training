package spittr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import spittr.service.activemq.ConsumerService;
import spittr.service.activemq.ProducerService;
import spittr.service.activemq.TopicConsumerService1;
import spittr.service.activemq.TopicProducerService;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 功能：
 * 条件：
 * Created by wq on 2017/3/21.
 */
//@Controller
public class ActiveMQDemoController {

    //队列名demo
//    @Resource(name="demoQueueDestination")
    private Destination demoQueueDestination;

//    队列消息生产者
//    @Resource(name="producerService")
    private ProducerService producer;

    //队列消息消费者
//    @Resource(name="consumerService")
    private ConsumerService consumer;


    //主题demo
//    @Resource(name="demoTopic")
    private Destination demoTopic;

    //主题消息生产者
//    @Resource(name="topicProducerService")
    private TopicProducerService producerTopic;

    //主题消息消费者
//    @Resource(name="topicConsumerService1")
    private TopicConsumerService1 consumerTopic;

    @RequestMapping(value="/welcome",method= RequestMethod.GET)
    public ModelAndView welcome(){
        System.out.println("------------进入JMS-Demo");
        ModelAndView mv = new ModelAndView();
        mv.setViewName("welcome");
        return mv;
    }

    @RequestMapping(value="/producer",method=RequestMethod.GET)
    public ModelAndView producer(){
        System.out.println("------------生产者活动");

        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format( now );
        System.out.println(time);

        ModelAndView mv = new ModelAndView();
        mv.addObject("time", time);
        mv.setViewName("producer");
        return mv;
    }

    @RequestMapping(value="/onsend",method=RequestMethod.POST)
    public ModelAndView producer(@RequestParam("message") String message) {
        System.out.println("------------发送消息");
        ModelAndView mv = new ModelAndView();
        producer.sendMessage(demoQueueDestination, message);
        mv.setViewName("welcome");
        return mv;
    }

    @RequestMapping(value="/receive",method=RequestMethod.GET)
    public ModelAndView queue_receive() throws JMSException {
        System.out.println("------------接收消息");
        ModelAndView mv = new ModelAndView();

        TextMessage tm = consumer.receive(demoQueueDestination);
        if(tm!= null) {
            mv.addObject("textMessage", tm.getText());
        }
        else {
            mv.addObject("textMessage", "消息被消费完了");
        }

        mv.setViewName("receive");
        return mv;
    }

    @RequestMapping(value="/topic_onsend",method=RequestMethod.POST)
    public ModelAndView topicProducer(@RequestParam("message") String message) {
        System.out.println("------------发送主题");
        ModelAndView mv = new ModelAndView();
        producerTopic.sendMessage(demoTopic, message);
        mv.setViewName("welcome");
        return mv;
    }

    @RequestMapping(value="/topic_receive",method=RequestMethod.GET)
    public ModelAndView topic_receive1() throws JMSException {
        System.out.println("------------接收主题");
        ModelAndView mv = new ModelAndView();

        TextMessage tm = consumerTopic.receive(demoTopic);
        if(tm!= null) {
            mv.addObject("textMessage", tm.getText());
        }
        else {
            mv.addObject("textMessage", "消息被消费完了");
        }

        mv.setViewName("receive");
        return mv;
    }

    /*
     * ActiveMQ Manager Test
     */
    @RequestMapping(value="/jms",method=RequestMethod.GET)
    public ModelAndView jmsManager() throws IOException {
        System.out.println("------------jms管理");
        ModelAndView mv = new ModelAndView();
        mv.setViewName("welcome");

        JMXServiceURL url = new JMXServiceURL("");
        JMXConnector connector = JMXConnectorFactory.connect(url);
        connector.connect();
        MBeanServerConnection connection = connector.getMBeanServerConnection();

        return mv;
    }
}
