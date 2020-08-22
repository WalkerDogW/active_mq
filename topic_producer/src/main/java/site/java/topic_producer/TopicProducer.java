package site.java.topic_producer;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
