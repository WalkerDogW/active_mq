package site.java.topic_producer;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author shkstart
 * @create 2020-08-21 20:21
 */
@RestController
public class TopicProducer {
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Value("${spring.activemq.topicName}")
    private String topicName;

    @RequestMapping("sendToTopic")
    public void sendMessage(String msg, HttpServletResponse response) throws IOException {

        //队列名称
        ActiveMQTopic topic = new ActiveMQTopic(topicName);

        //发送消息
        jmsMessagingTemplate.convertAndSend(topic, msg);

        System.out.println("客户端发送消息成功");

        response.getWriter().write("success");
    }


    @RequestMapping("sendToTopic2")
    public void sendMessage2() throws JMSException {
        String topicName = "myTopic_new";

        ConnectionFactory connectionFactory = jmsMessagingTemplate.getConnectionFactory();
        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Topic topic = session.createTopic(topicName);
        MessageProducer messageProducer = session.createProducer(topic);


        for (int i = 0; i < 3; i++) {
            TextMessage textMessage = session.createTextMessage("Topic: " + i);
            messageProducer.send(textMessage);
        }

        messageProducer.close();
        session.close();
        connection.close();
        System.out.println("信息发布到Topic:"+topicName);
    }



    //持久化
    @RequestMapping("sendToTopic3")
    public void sendMessage3() throws JMSException {
        String topicName = "myTopic_durable";

        ConnectionFactory connectionFactory = jmsMessagingTemplate.getConnectionFactory();
        Connection connection = connectionFactory.createConnection();
        //topic模式，要做持久化需要先开启持久化再启动链接
//        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Topic topic = session.createTopic(topicName);
        MessageProducer messageProducer = session.createProducer(topic);

        //topic模式，要做持久化需要先开启持久化再启动链接
        messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
        connection.start();

        for (int i = 0; i < 3; i++) {
            TextMessage textMessage = session.createTextMessage("Topic: " + i);
            messageProducer.send(textMessage);
        }

        messageProducer.close();
        session.close();
        connection.close();
        System.out.println("信息发布到Topic:"+topicName);
    }




}
