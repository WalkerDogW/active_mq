package site.java.producer;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
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
public class QueueProducer {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @RequestMapping("sendToQueue")
    public void sendMessage(String msg, HttpServletResponse response) throws IOException {

        //队列名称
        ActiveMQQueue queue = new ActiveMQQueue("myQueue");

        //发送消息
        jmsMessagingTemplate.convertAndSend(queue, msg);

        System.out.println("客户端发送消息成功");

        response.getWriter().write("success");
    }


    @RequestMapping("sendToQueue2")
    public void sendMessage2() throws JMSException {
        //通过链接工厂获取connection并启动
        ConnectionFactory connectionFactory = jmsMessagingTemplate.getConnectionFactory();
        Connection connection = connectionFactory.createConnection();
        connection.start();

        //创建会话，参数为事务，签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //创建目的地
        Queue myQueue2 = session.createQueue("myQueue2");

        //创建生产者
        MessageProducer messageProducer = session.createProducer(myQueue2);

        //生产消息
        for (int i=0 ; i<3;i++){
            TextMessage textMessage = session.createTextMessage("msg----" + i);
            messageProducer.send(textMessage);
        }

        //关闭资源
        messageProducer.close();
        connection.close();
        System.out.println("信息发布到MQ完成");
    }
}
